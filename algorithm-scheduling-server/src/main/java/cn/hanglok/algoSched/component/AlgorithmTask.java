package cn.hanglok.algoSched.component;

import cn.hanglok.algoSched.entity.Template;
import cn.hanglok.algoSched.service.DockerService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CancellationException;
import java.util.concurrent.CountDownLatch;

/**
 * @author Allen
 * @version 1.0
 * @className AlgorithmTaskList
 * @description TODO
 * @date 2024/3/14
 */
@Slf4j
public class AlgorithmTask implements Runnable {
    private final String taskId;
    @Getter
    private final Template.AlgorithmModel algorithmModel;

    @Getter
    private final CountDownLatch latch = new CountDownLatch(1);

    private final CountDownLatch preLatch;

    private final DockerService dockerService;

    public AlgorithmTask(String taskId, Template.AlgorithmModel algorithmModel, CountDownLatch preLatch, DockerService dockerService) {
        this.taskId = taskId;
        this.algorithmModel = algorithmModel;
        this.preLatch = preLatch;
        this.dockerService = dockerService;
    }

    @Override
    public void run() {
        if (null != preLatch) {
            try {
                log.info("AlgorithmTask is waiting: " + taskId + "," +  algorithmModel.getImage());
                preLatch.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        log.info("AlgorithmTask is running: " + taskId + "," +  algorithmModel.getImage());

        try {
            String containerId = dockerService.execute1(taskId, algorithmModel, "0");
            log.info("containerId: " + containerId);

            latch.await();
        } catch (CancellationException | InterruptedException e) {
            log.error("task was interrupted: " + taskId + "," +  algorithmModel.getImage());
        }

    }
}
