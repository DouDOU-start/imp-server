package cn.hanglok.algoSched.service;

import java.io.IOException;

/**
 * @author Allen
 * @version 1.0
 * @className DockerService
 * @description TODO
 * @date 2023/12/26
 */
public interface DockerService {
    void execute(String taskId, String image, String execEnvJson, String singleGpu);
    void executeLungSegmentation(String taskId, String inputFile) throws IOException;
//    void mergeLungSegmentation(String taskId);
}
