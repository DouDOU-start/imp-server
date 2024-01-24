package cn.hanglok.algoSched.service.impl;

import cn.hanglok.algoSched.config.DockerConfig;
import cn.hanglok.algoSched.config.MinioConfig;
import cn.hanglok.algoSched.config.TemplateConfig;
import cn.hanglok.algoSched.entity.TaskLog;
import cn.hanglok.algoSched.entity.TaskQueue;
import cn.hanglok.algoSched.entity.Template;
import cn.hanglok.algoSched.service.DockerService;
import cn.hanglok.algoSched.service.MinioService;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.io.File;
import java.io.IOException;
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
    TemplateConfig templateConfig;

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
    public void execute(String taskId, String image, String execEnvJson, String singleGpu) {

        DockerClient dockerClient = getDockerClient();

        log.info("execute algorithm: " + new HashMap<>() {{
            put("image", image);
            put("execEnvJson", execEnvJson);
        }});

        String execEnv = String.format("EXEC_ENV=%s", execEnvJson);
        String minioEnv = String.format("MINIO_ENV=%s", new JSONObject() {{
            put("url", minioConfig.getInnerUrl());
            put("access_key", minioConfig.getAccessKey());
            put("secret_key", minioConfig.getSecretKey());
        }});


        // TODO 融合算法暂时使用GPU模版
        HostConfig hostConfig = null;

        if (! "-1".equals(singleGpu)) {
            DeviceRequest deviceRequest = new DeviceRequest();

            List<List<String>> capabilities = new ArrayList<>() {{
                add(new ArrayList<>() {{
                    add("gpu");
                }});
            }};

            hostConfig = new HostConfig().withDeviceRequests(
                    new ArrayList<>() {{
                        add(deviceRequest.withDriver("")
                                .withCount(0)
                                .withDeviceIds(Collections.singletonList(singleGpu))
                                .withOptions(new HashMap<>())
                                .withCapabilities(capabilities));
                    }}
            ).withShmSize(1024L * 1024L * 1024L);
        }

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
    public void executeLungSegmentation(String taskId, String inputFile) throws IOException {

        long startTime = System.currentTimeMillis();

        ObjectMapper objectMapper = new ObjectMapper();
        Template template = objectMapper.readValue(new File(templateConfig.getPath()), Template.class);
        template.getAlgorithms().forEach(algorithmModel -> {
            execute(
                    taskId,
                    "hanglok/" +  algorithmModel.getImage() + ":" + algorithmModel.getVersion(),
                    Template.createExecEnvJson(taskId, inputFile, algorithmModel),
                    algorithmModel.getGpu()
            );
            if (null != algorithmModel.getChild()) {
                execute(
                        taskId,
                        "hanglok/" +  algorithmModel.getChild().getImage() + ":" + algorithmModel.getChild().getVersion(),
                        Template.createExecEnvJson(taskId, inputFile, algorithmModel.getChild()),
                        algorithmModel.getChild().getGpu()
                );
            }
        });

        String[] objects = template.getResult().stream().map(r -> r.replace("%s", taskId)).toArray(String[]::new);

        minioService.zipObject(objects, String.format("output/%s/result.zip", taskId));

        TaskQueue.value.put(taskId, new TaskQueue.Field(
                taskId,
                "completed",
                minioService.getObjectUrl(String.format("output/%s/result.zip", taskId), 60 * 30),
                String.format("%s ms", System.currentTimeMillis() - startTime)));
    }

//    @SneakyThrows
//    @Override
//    @Deprecated
//    public void mergeLungSegmentation(String taskId) {
//
//        DockerClient dockerClient = getDockerClient();
//
//        String execEnvJson = new JSONObject() {{
//            put("task_id", taskId);
//            put("input", new ArrayList<>() {{
//                add(new HashMap<>() {{
//                    put("object_name", String.format("output/%s/BodyInference-0.1.3/body_inference.nii.gz", taskId));
//                    put("label", Map.of("Skin", 3, "Bone", 2).entrySet().stream().toList());
//                }});
//                add(new HashMap<>() {{
//                    put("object_name", String.format("output/%s/LungSegmentation-0.1.3/lungsegmentation.nii.gz", taskId));
//                    put("label", Map.of("Lung", true).entrySet().stream().toList());
//                }});
//                add(new HashMap<>() {{
//                    put("object_name", String.format("output/%s/AirwaySegmentation-0.1.3-jcxiong/airwaysegmentation.nii.gz", taskId));
//                    put("label", Map.of("Airway", 1).entrySet().stream().toList());
//                }});
//            }});
//            put("output", "segmentation.mha");
//        }}.toString();
//
//        log.info("execute algorithm: " + new HashMap<>() {{
//            put("image", "hanglok/fusion:0.0.1");
//            put("execEnvJson", execEnvJson);
//        }});
//
//        String execEnv = String.format("EXEC_ENV=%s", execEnvJson);
//
//        String minioEnv = String.format("MINIO_ENV=%s", new JSONObject() {{
//            put("url", minioConfig.getInnerUrl());
//            put("access_key", minioConfig.getAccessKey());
//            put("secret_key", minioConfig.getSecretKey());
//        }});
//
//        // 创建容器
//        CreateContainerResponse container = dockerClient.createContainerCmd("hanglok/fusion:0.0.1")
//                .withEnv(execEnv, minioEnv)
//                .exec();
//
//        // 启动容器
//        dockerClient.startContainerCmd(container.getId()).exec();
//
//        // Retrieve and print logs
//        dockerClient.logContainerCmd(container.getId())
//                .withStdOut(true)
//                .withStdErr(true)
//                .withFollowStream(true)
//                .exec(new ResultCallback.Adapter<Frame>() {
//                    @Override
//                    public void onNext(Frame frame) {
//                        Map<String, StringBuilder> algorithmMap = TaskLog.value.getOrDefault(taskId, new HashMap<>());
//                        StringBuilder logg = algorithmMap.getOrDefault("hanglok/fusion:0.0.1", new StringBuilder());
//                        logg.append(new String(frame.getPayload()));
//                        algorithmMap.put("hanglok/fusion:0.0.1", logg);
//                        TaskLog.value.put(taskId, algorithmMap);
//                        log.debug(new String(frame.getPayload()));
//                    }
//                })
//                .awaitCompletion();
//
//        dockerClient.removeContainerCmd(container.getId()).exec();
//
//        dockerClient.close();
//    }
}
