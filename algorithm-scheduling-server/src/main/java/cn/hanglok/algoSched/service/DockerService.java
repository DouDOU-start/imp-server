package cn.hanglok.algoSched.service;

/**
 * @author Allen
 * @version 1.0
 * @className DockerService
 * @description TODO
 * @date 2023/12/26
 */
public interface DockerService {
    void execute(String taskId, String image, String singleGpu, String url, String output);
    void executeLungSegmentation(String taskId, String inputFile);
    void mergeLungSegmentation(String taskId);
}
