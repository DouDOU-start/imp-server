package cn.hanglok.algoSched.component;

import cn.hanglok.algoSched.entity.TaskQueue;
import cn.hanglok.algoSched.service.DockerService;
import cn.hanglok.algoSched.service.MinioService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.concurrent.*;

/**
 * @author Allen
 * @version 1.0
 * @className AlgorithmTaskExecutor
 * @description TODO
 * @date 2024/1/27
 */
@Data
@Slf4j
@Component
public class AlgorithmTaskExecutor {

    @Autowired
    MinioService minioService;
    @Autowired
    DockerService dockerService;
    private final ExecutorService taskExecutor = Executors.newSingleThreadExecutor();
    private final ScheduledExecutorService timeoutScheduler = Executors.newScheduledThreadPool(1);
    private Future<?> currentTaskFuture = null;
    private boolean isTaskWaiting = false;
    private final Object lock = new Object();

    public boolean execute(String taskId, MultipartFile file) {

        synchronized (lock) {
            if (currentTaskFuture != null && ! currentTaskFuture.isDone()) {
                if (isTaskWaiting) {
                    log.warn("Algorithm task queue is busy...");
                    // 已有一个任务在执行和一个任务在等待，拒绝新任务
                    return false;
                }
                isTaskWaiting = true; // 标记有一个任务在等待
            }

            Runnable task = () -> {
                log.info(taskId + ": Algorithm is running...");

                TaskQueue.value.put(taskId, new TaskQueue.Field(taskId,"running", null, null));

                try {
                    dockerService.executeLungSegmentation(taskId, String.format("input/%s/%s", taskId, file.getOriginalFilename()));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                synchronized (lock) {
                    isTaskWaiting = false; // 更新等待标志
                }
            };

            minioService.uploadFile(file, String.format("/input/%s/", taskId));

            TaskQueue.value.put(taskId, new TaskQueue.Field(taskId,"waiting", null, null));

            // 提交新任务
            currentTaskFuture = taskExecutor.submit(task);

            // 设置超时时间为5分钟
            timeoutScheduler.schedule(() -> {

                log.error(taskId, ": timeout error.");

                synchronized (lock) {
                    if (! currentTaskFuture.isDone()) {
                        currentTaskFuture.cancel(true); // 如果任务还在运行，则取消它
                        TaskQueue.value.put(taskId, new TaskQueue.Field(taskId,"failed", null, null));
                    }
                    isTaskWaiting = false; // 更新等待标志
                }
            }, 5, TimeUnit.MINUTES);

            return true;

        }

    }
}
