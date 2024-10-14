package cn.hanglok.algoSched.service.impl;

import cn.hanglok.algoSched.config.CallbackConfig;
import cn.hanglok.algoSched.config.DockerConfig;
import cn.hanglok.algoSched.config.MinioConfig;
import cn.hanglok.algoSched.config.MySQLConfig;
import cn.hanglok.algoSched.entity.Template;
import cn.hanglok.algoSched.service.DockerService;
import cn.hanglok.algoSched.service.MinioService;
import com.alibaba.fastjson.JSONObject;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.DeviceRequest;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    MySQLConfig mySQLConfig;

    @Autowired
    DockerConfig dockerConfig;

    @Autowired
    CallbackConfig callbackConfig;

    private static DockerClient dockerClient;

    public DockerClient getDockerClient() {

        if (dockerClient == null) {
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

            dockerClient =  DockerClientImpl.getInstance(config, httpClient);
        }

        return dockerClient;
    }

    public void close() throws IOException {
        if (null != dockerClient) {
            dockerClient.close();
        }
    }

    public String execute(String taskId, Template.AlgorithmModel algorithmModel, String singleGpu) {

        DockerClient dockerClient = getDockerClient();

        // 暂时不从仓库拉取镜像
//        try {
//            dockerClient.pullImageCmd(algorithmModel.getImage())
//                    .exec(new PullImageResultCallback())
//                    .awaitCompletion();
//        } catch (InterruptedException e) {
//            log.error("Image does not exist:  {}", algorithmModel.getImage());
//            throw new RuntimeException(e);
//        }

        String execEnvJson = algorithmModel.createExecEnvJson(taskId, callbackConfig.getUrl());

        String execEnv = String.format("EXEC_ENV=%s", execEnvJson);
        String minioEnv = String.format("MINIO_ENV=%s", new JSONObject() {{
            put("url", minioConfig.getInnerUrl());
            put("access_key", minioConfig.getAccessKey());
            put("secret_key", minioConfig.getSecretKey());
        }});
        String mySQLEnv = String.format("MYSQL_ENV=%s", new JSONObject() {{
            put("host", mySQLConfig.getHost());
            put("port", mySQLConfig.getPort());
            put("user", mySQLConfig.getUserName());
            put("password", mySQLConfig.getPassword());
            put("database", mySQLConfig.getDatabase());
        }});

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
            ).withShmSize(1024L * 1024L * 1024L).withNetworkMode("host");
        }

        // 创建容器
        CreateContainerResponse container = dockerClient.createContainerCmd(algorithmModel.getImage())
                .withEnv(execEnv, minioEnv, mySQLEnv)
                .withHostConfig(hostConfig)
                .exec();

        // 启动容器
        dockerClient.startContainerCmd(container.getId()).exec();

        return container.getId();
    }

}
