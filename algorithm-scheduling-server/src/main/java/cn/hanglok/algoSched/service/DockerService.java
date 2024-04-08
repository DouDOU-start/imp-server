package cn.hanglok.algoSched.service;

import cn.hanglok.algoSched.entity.Template;
import com.github.dockerjava.api.DockerClient;

/**
 * @author Allen
 * @version 1.0
 * @className DockerService
 * @description TODO
 * @date 2023/12/26
 */
public interface DockerService {

    DockerClient getDockerClient();
    String execute(String taskId, Template.AlgorithmModel algorithmModel, String singleGpu);
}
