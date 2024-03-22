package cn.hanglok.algoSched.component;

import cn.hanglok.algoSched.dto.AlgorithmCallback;
import cn.hanglok.algoSched.entity.TaskStdout;
import cn.hanglok.algoSched.entity.TaskQueue;
import cn.hanglok.algoSched.entity.Template;
import cn.hanglok.algoSched.service.MinioService;
import io.minio.errors.*;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Allen
 * @version 1.0
 * @className AlgorithmAssembleMonitor
 * @description TODO
 * @date 2024/3/14
 */
@Slf4j
public class AlgorithmAssembleMonitor {

    private final String taskId;
    private final ExecutorService executorService = Executors.newFixedThreadPool(1);
    private final Map<String, AlgorithmTask> algorithmTaskMap = new ConcurrentHashMap<>();
    private final AtomicInteger successfulTasks = new AtomicInteger(0);
    private final AtomicBoolean anyTaskFailed = new AtomicBoolean(false);
    private final CountDownLatch latch;
    private final Template template;
    private final List<AlgorithmCallback> callbackList = Collections.synchronizedList(new ArrayList<>());
    private AlgorithmCallback failedBack;

    @Setter
    private long startTime;
    private final MinioService minioService;

    public AlgorithmAssembleMonitor(String taskId, Template template, HanglokAlgorithm hanglokAlgorithm, MinioService minioService) {
        this.taskId = taskId;
        this.template = template;
        this.latch = new CountDownLatch(template.calculateNum());
        this.minioService = minioService;
        hanglokAlgorithm.addAlgorithmAssembleMonitor(taskId, this);
    }

    public void submitTask(AlgorithmTask task) {
        executorService.execute(task);
        algorithmTaskMap.put(task.getAlgorithmModel().getImage(), task);

        // 模板算法列表预记录
        Map<String, String> algorithmStdoutMap = TaskStdout.value.getOrDefault(taskId, new HashMap<>());
        algorithmStdoutMap.put(task.getAlgorithmModel().getImage(), "");
        TaskStdout.value.put(taskId, algorithmStdoutMap);
    }

    public void shutdown() {
        executorService.shutdown(); // 关闭线程池
    }

    public void taskCallback(AlgorithmCallback callback) {

        Map<String, String> algorithmMap = TaskStdout.value.getOrDefault(taskId, new HashMap<>());
        algorithmMap.put(callback.getImage(), callback.getStdout());
        TaskStdout.value.put(taskId, algorithmMap);

        if (callback.getIsSuccess()) {
            log.info("AlgorithmTask execute success: " + taskId + "," +  callback.getImage());
            AlgorithmTask algorithmTask = algorithmTaskMap.get(callback.getImage());
            algorithmTask.getLatch().countDown();
            successfulTasks.incrementAndGet();
            latch.countDown();
        } else {
            log.error("AlgorithmTask execute failed: " + taskId + "," +  callback.getImage());
            executorService.shutdownNow();
            anyTaskFailed.set(true);
            while (latch.getCount() > 0) {
                latch.countDown();
            }
            failedBack = callback;
        }

        callbackList.add(callback);

    }

    public void awaitCompletion() {

        TaskQueue.value.put(taskId, new TaskQueue.Field(taskId,"running", null, null, null));

        new Thread(() -> {
            try {
                latch.await();

                if (anyTaskFailed.get()) {
                    TaskQueue.value.put(taskId, new TaskQueue.Field(
                            taskId,"failed",
                            null,
                            String.format("%s ms", System.currentTimeMillis() - startTime),
                            failedBack.getStdout()));
                    log.error("Algorithm assemble execute failed: " + taskId);
                } else {
                    String[] objects = template.getResult().stream().map(r -> taskId + "/" + r).toArray(String[]::new);
                    minioService.zipObject(objects, String.format("%s/result.zip", taskId));
                    TaskQueue.value.put(taskId, new TaskQueue.Field(
                            taskId,
                            "completed",
                            minioService.getObjectUrl(String.format("%s/result.zip", taskId), 60 * 30),
                            String.format("%s ms", System.currentTimeMillis() - startTime), null));
                    log.info("Algorithm assemble execute success: " + taskId);
                }

            } catch (InternalException | XmlParserException | InvalidResponseException |
                     InvalidKeyException | NoSuchAlgorithmException | IOException | ErrorResponseException |
                     InsufficientDataException | ServerException e) {
                log.error("minio exception: " + e);
            } catch (InterruptedException e) {
                log.error(e.toString());
            }
        }).start();

    }
}
