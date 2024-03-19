package cn.hanglok.algoSched.component;

import cn.hanglok.algoSched.entity.Assembles;
import cn.hanglok.algoSched.entity.TaskQueue;
import cn.hanglok.algoSched.entity.Template;
import cn.hanglok.algoSched.exception.TemplateErrorException;
import cn.hanglok.algoSched.service.DockerService;
import cn.hanglok.algoSched.service.IAssemblesService;
import cn.hanglok.algoSched.service.MinioService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    @Autowired
    IAssemblesService assemblesService;

    private final ExecutorService executorService = Executors.newFixedThreadPool(1);

    public void execute(String taskId, String assembleName, MultipartFile file) {
        Assembles as = assemblesService.getOne(new QueryWrapper<>() {{
            eq("name", assembleName);
        }});

        if (null == as) {
            throw new TemplateErrorException("Not found the corresponding template: " + assembleName);
        }

        TaskQueue.value.put(taskId, new TaskQueue.Field(taskId,"waiting", null, null, null));

        executorService.execute(() -> {
            minioService.uploadFile(file, String.format("/%s/", taskId));

            TaskQueue.value.put(taskId, new TaskQueue.Field(taskId,"running", null, null, null));

            ObjectMapper objectMapper = new ObjectMapper();
            Template template;
            try {
                template = objectMapper.readValue(as.getData(), Template.class);
            } catch (JsonProcessingException e) {
                log.error("template parse error: " + e);
                throw new RuntimeException(e);
            }

            AlgorithmAssembleMonitor algorithmAssembleMonitor = new AlgorithmAssembleMonitor(taskId, template, hanglokAlgorithm, minioService);

            template.execute(taskId, algorithmAssembleMonitor, dockerService);

            algorithmAssembleMonitor.awaitCompletion();
            algorithmAssembleMonitor.shutdown();
        });
    }
}
