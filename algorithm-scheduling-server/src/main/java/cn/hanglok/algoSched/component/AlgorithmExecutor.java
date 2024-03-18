package cn.hanglok.algoSched.component;

import cn.hanglok.algoSched.entity.TaskQueue;
import cn.hanglok.algoSched.entity.Template;
import cn.hanglok.algoSched.service.DockerService;
import cn.hanglok.algoSched.service.MinioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

/**
 * @author Allen
 * @version 1.0
 * @className AlgorithmExecutor
 * @description TODO
 * @date 2024/3/14
 */
@Component
public class AlgorithmExecutor {

    @Autowired
    HanglokAlgorithm hanglokAlgorithm;

    @Autowired
    DockerService dockerService;

    @Autowired
    MinioService minioService;

    public void execute(String taskId) {

        ObjectMapper objectMapper = new ObjectMapper();
        Template template;
        try {
            template = objectMapper.readValue(new File("/Users/allen/code/imp-server/algorithm-scheduling-server/src/main/resources/template1.json"), Template.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        AlgorithmAssembleMonitor algorithmAssembleMonitor = new AlgorithmAssembleMonitor(taskId, template, hanglokAlgorithm, minioService);

        template.execute(taskId, algorithmAssembleMonitor, dockerService);

        algorithmAssembleMonitor.awaitCompletion();
        algorithmAssembleMonitor.shutdown();

    }
}
