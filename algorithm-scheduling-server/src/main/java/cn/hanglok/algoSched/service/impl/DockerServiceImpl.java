package cn.hanglok.algoSched.service.impl;

import cn.hanglok.algoSched.config.DockerConfig;
import cn.hanglok.algoSched.config.MinioConfig;
import cn.hanglok.algoSched.entity.TaskLog;
import cn.hanglok.algoSched.entity.TaskQueue;
import cn.hanglok.algoSched.service.DockerService;
import cn.hanglok.algoSched.service.MinioService;
import com.alibaba.fastjson.JSONObject;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.DeviceRequest;
import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;

/**
 * @author Allen
 * @version 1.0
 * @className DockerServiceImpl
 * @description TODO
 * @date 2023/12/26
 */
@Slf4j
@Service
public class DockerServiceImpl implements DockerService {

    @Autowired
    MinioService minioService;

    @Autowired
    MinioConfig minioConfig;

    @Autowired
    DockerConfig dockerConfig;

    public DockerClient getDockerClient() {
        DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
                .withDockerHost(dockerConfig.getHost())
                .withDockerTlsVerify(false)
                .build();

        DockerHttpClient httpClient = new ApacheDockerHttpClient.Builder()
                .dockerHost(config.getDockerHost())
                .sslConfig(config.getSSLConfig())
                .maxConnections(100)
                .connectionTimeout(Duration.ofSeconds(30))
                .responseTimeout(Duration.ofSeconds(300))
                .build();

        return DockerClientImpl.getInstance(config, httpClient);
    }

    @SneakyThrows
    @Override
    public void execute(String taskId, String image, String singleGpu, String url, String output) {

        DockerClient dockerClient = getDockerClient();

        String execEnvJson = new JSONObject() {{
            put("task_id", taskId);
            put("input", Map.of("url", url).entrySet().stream().toList());
            put("output", output);
        }}.toString();

        log.info("execute algorithm: " + new HashMap<>() {{
            put("image", image);
            put("execEnvJson", execEnvJson);
        }});

        String execEnv = String.format("EXEC_ENV=%s", execEnvJson);
        String minioEnv = String.format("MINIO_ENV=%s", new JSONObject() {{
            put("url", minioConfig.getUrl().replace("http://", ""));
            put("access_key", minioConfig.getAccessKey());
            put("secret_key", minioConfig.getSecretKey());
        }});

        DeviceRequest deviceRequest = new DeviceRequest();

        List<List<String>> capabilities = new ArrayList<>() {{
            add(new ArrayList<>() {{
                add("gpu");
            }});
        }};

        HostConfig hostConfig = new HostConfig().withDeviceRequests(
                new ArrayList<>() {{
                    add(deviceRequest.withDriver("")
                            .withCount(0)
                            .withDeviceIds(Collections.singletonList(singleGpu))
                            .withOptions(new HashMap<>())
                            .withCapabilities(capabilities));
                }}
        ).withShmSize(1024L * 1024L * 1024L);;

        // 创建容器
        CreateContainerResponse container = dockerClient.createContainerCmd(image)
                .withEnv(execEnv, minioEnv)
                .withHostConfig(hostConfig)
                .exec();

        // 启动容器
        dockerClient.startContainerCmd(container.getId()).exec();

        // Retrieve and print logs
        dockerClient.logContainerCmd(container.getId())
                .withStdOut(true)
                .withStdErr(true)
                .withFollowStream(true)
                .exec(new ResultCallback.Adapter<Frame>() {
                    @Override
                    public void onNext(Frame frame) {
                        Map<String, StringBuilder> algorithmMap = TaskLog.value.getOrDefault(taskId, new HashMap<>());
                        StringBuilder logg = algorithmMap.getOrDefault(image, new StringBuilder());
                        logg.append(new String(frame.getPayload()));
                        algorithmMap.put(image, logg);
                        TaskLog.value.put(taskId, algorithmMap);
                        log.debug(new String(frame.getPayload()));
                    }
                })
                .awaitCompletion();

        dockerClient.removeContainerCmd(container.getId()).exec();

        dockerClient.close();

    }

    @Async
    @Override
    public void executeLungSegmentation(String taskId, String objectUrl) {

        long startTime = System.currentTimeMillis();

        execute(
                taskId,
                "hanglok/lungsegmentation:0.1.3",
                "0",
                objectUrl,
                "lungsegmentation.nii.gz"
        );
        execute(
                taskId,
                "hanglok/airwaysegmentation:0.1.3-jcxiong",
                "0",
                objectUrl,
                "airwaysegmentation.nii.gz"
        );
        execute(
                taskId,
                "hanglok/centerline_datastructure:1023",
                "0",
                minioService.getObjectUrl(String.format("/output/%s/AirwaySegmentation-0.1.3-jcxiong/airwaysegmentation.nii.gz",
                        taskId), 60 * 30),
                "centerline.txt"
        );
        execute(
                taskId,
                "hanglok/bodyinference:0.1.3",
                "0",
                objectUrl,
                "body_inference.nii.gz"
        );
        execute(
                taskId,
                "hanglok/nodule_detection:2023_12_6",
                "0",
                objectUrl,
                "target.json"
        );

        mergeLungSegmentation(taskId);

        String[] objects = {
                String.format("output/%s/Fusion-0.0.1/segmentation.mha", taskId),
                String.format("output/%s/centerline_datastructure-1023/centerline.txt", taskId),
                String.format("output/%s/nodule_detection-2023_12_6/target.json", taskId)
        };

        minioService.zipObject(objects, String.format("output/%s/result.zip", taskId));

        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;

        TaskQueue.value.put(taskId, new TaskQueue.Field(
                taskId,
                "completed",
                minioService.getObjectUrl(String.format("output/%s/result.zip", taskId), 60 * 30),
                String.format("%s ms", executionTime)));
    }

    @SneakyThrows
    @Override
    public void mergeLungSegmentation(String taskId) {

        DockerClient dockerClient = getDockerClient();

        String execEnvJson = new JSONObject() {{
            put("task_id", taskId);
            put("input", new ArrayList<>() {{
                add(new HashMap<>() {{
                    put("object_name", String.format("output/%s/BodyInference-0.1.3/body_inference.nii.gz", taskId));
                    put("label", Map.of("Skin", 3, "Bone", 2).entrySet().stream().toList());
                }});
                add(new HashMap<>() {{
                    put("object_name", String.format("output/%s/LungSegmentation-0.1.3/lungsegmentation.nii.gz", taskId));
                    put("label", Map.of("Lung", true).entrySet().stream().toList());
                }});
                add(new HashMap<>() {{
                    put("object_name", String.format("output/%s/AirwaySegmentation-0.1.3-jcxiong/airwaysegmentation.nii.gz", taskId));
                    put("label", Map.of("Airway", 1).entrySet().stream().toList());
                }});
            }});
            put("output", "segmentation.mha");
        }}.toString();

        log.info("execute algorithm: " + new HashMap<>() {{
            put("image", "hanglok/fusion:0.0.1");
            put("execEnvJson", execEnvJson);
        }});

        String execEnv = String.format("EXEC_ENV=%s", execEnvJson);

        String minioEnv = String.format("MINIO_ENV=%s", new JSONObject() {{
            put("url", minioConfig.getUrl().replace("http://", ""));
            put("access_key", minioConfig.getAccessKey());
            put("secret_key", minioConfig.getSecretKey());
        }});

        // 创建容器
        CreateContainerResponse container = dockerClient.createContainerCmd("hanglok/fusion:0.0.1")
                .withEnv(execEnv, minioEnv)
                .exec();

        // 启动容器
        dockerClient.startContainerCmd(container.getId()).exec();

        // Retrieve and print logs
        dockerClient.logContainerCmd(container.getId())
                .withStdOut(true)
                .withStdErr(true)
                .withFollowStream(true)
                .exec(new ResultCallback.Adapter<Frame>() {
                    @Override
                    public void onNext(Frame frame) {
                        Map<String, StringBuilder> algorithmMap = TaskLog.value.getOrDefault(taskId, new HashMap<>());
                        StringBuilder logg = algorithmMap.getOrDefault("hanglok/fusion:0.0.1", new StringBuilder());
                        logg.append(new String(frame.getPayload()));
                        algorithmMap.put("hanglok/fusion:0.0.1", logg);
                        TaskLog.value.put(taskId, algorithmMap);
                        log.debug(new String(frame.getPayload()));
                    }
                })
                .awaitCompletion();

        dockerClient.removeContainerCmd(container.getId()).exec();

        dockerClient.close();
    }
}
