package cn.hanglok.algoSched.component;

import cn.hanglok.algoSched.entity.TaskQueue;
import cn.hanglok.algoSched.entity.Template;
import cn.hanglok.algoSched.service.DockerService;
import cn.hanglok.algoSched.service.MinioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Allen
 * @version 1.0
 * @className AlgorithmExecutor
 * @description TODO
 * @date 2024/3/14
 */
@Slf4j
@Component
public class AlgorithmExecutor {

    @Autowired
    HanglokAlgorithm hanglokAlgorithm;

    @Autowired
    DockerService dockerService;

    @Autowired
    MinioService minioService;

    private final ExecutorService executorService = Executors.newFixedThreadPool(1);

    public void execute(String taskId, Template template, MultipartFile file) {

        TaskQueue.value.put(taskId, new TaskQueue.Field(taskId,"waiting", null, null, null));

        executorService.execute(() -> {
            minioService.uploadFile(file, String.format("/%s/", taskId));

            TaskQueue.value.put(taskId, new TaskQueue.Field(taskId,"running", null, null, null));

            AlgorithmAssembleMonitor algorithmAssembleMonitor = new AlgorithmAssembleMonitor(taskId, template, hanglokAlgorithm, minioService);

            template.execute(taskId, algorithmAssembleMonitor, dockerService);

            algorithmAssembleMonitor.awaitCompletion();
            algorithmAssembleMonitor.shutdown();
        });
    }
}
