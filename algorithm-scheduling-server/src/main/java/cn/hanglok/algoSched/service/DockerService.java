package cn.hanglok.algoSched.service;

import cn.hanglok.algoSched.entity.Template;

import java.io.IOException;

/**
 * @author Allen
 * @version 1.0
 * @className DockerService
 * @description TODO
 * @date 2023/12/26
 */
public interface DockerService {
    void execute(String taskId, Template.AlgorithmModel algorithmModel, String singleGpu);
    void execute(String taskId, Template template) throws IOException;
    String execute1(String taskId, Template.AlgorithmModel algorithmModel, String singleGpu);
}
