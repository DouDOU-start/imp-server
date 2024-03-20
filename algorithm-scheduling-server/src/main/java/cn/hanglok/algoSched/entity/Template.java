package cn.hanglok.algoSched.entity;

import cn.hanglok.algoSched.component.AlgorithmAssembleMonitor;
import cn.hanglok.algoSched.component.AlgorithmTask;
import cn.hanglok.algoSched.service.DockerService;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * @author Allen
 * @version 1.0
 * @className Template
 * @description TODO
 * @date 2024/1/18
 */
@Slf4j
@Data
public class Template {
    private List<AlgorithmModel> algorithms;
    private List<String> result;

    @Data
    public static class AlgorithmModel {
        private String image;
        private Map<String, Object> args;
        private Map<String, String> inputFile;
        private Map<String, String> outputFile;
        private AlgorithmModel child;

        public String createExecEnvJson(String taskId, String callback) {
            return new JSONObject() {{
                put("task_id", taskId);
                put("args", args);
                put("inputFile", inputFile);
                put("outputFile", outputFile);
                put("image", image);
                put("callback", callback);
            }}.toString();
        }
    }

    public static Template load(String as) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(as, Template.class);
        } catch (JsonProcessingException e) {
            log.error("template string parse error: " + e);
            throw new RuntimeException(e);
        }
    }
    public static Template load(MultipartFile file) {
        try {
            String jsonStr = new String(file.getBytes());
            return JSON.parseObject(jsonStr, Template.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (JSONException e) {
            log.error("template file parse error.");
            throw new RuntimeException(e);
        }
    }

    public static String createExecEnvJson(String taskId, AlgorithmModel algorithmModel) {
        return new JSONObject() {{
            put("task_id", taskId);
            put("args", algorithmModel.getArgs());
            put("inputFile", algorithmModel.getInputFile());
            put("outputFile", algorithmModel.getOutputFile());
        }}.toString();
    }

    public int calculateNum() {
        int num = 0;
        for (AlgorithmModel algorithmModel : algorithms) {
            num ++;
            if (null != algorithmModel.getChild()) {
                num = calculateChildNum(algorithmModel.getChild(), num);
            }
        }
        return num;
    }

    public int calculateChildNum(AlgorithmModel algorithmModel, int num) {
        num ++;
        if (null != algorithmModel.getChild()) {
            num = calculateChildNum(algorithmModel.getChild(), num);
        }
        return num;
    }

    public void execute(String taskId, AlgorithmAssembleMonitor algorithmAssembleMonitor, DockerService dockerService) {
        algorithmAssembleMonitor.setStartTime(System.currentTimeMillis());
        for (AlgorithmModel algorithmModel : algorithms) {
            AlgorithmTask task = new AlgorithmTask(taskId, algorithmModel, null, dockerService);
            algorithmAssembleMonitor.submitTask(task);
            if (null != algorithmModel.getChild()) {
                executeChild(taskId, algorithmAssembleMonitor, algorithmModel.getChild(), task.getLatch(), dockerService);
            }
        }
    }

    public void executeChild(String taskId, AlgorithmAssembleMonitor algorithmAssembleMonitor, AlgorithmModel algorithmModel, CountDownLatch preLatch, DockerService dockerService) {
        AlgorithmTask task = new AlgorithmTask(taskId, algorithmModel, preLatch, dockerService);
        algorithmAssembleMonitor.submitTask(task);
        if (null != algorithmModel.getChild()) {
            executeChild(taskId, algorithmAssembleMonitor, algorithmModel.getChild(), task.getLatch(), dockerService);
        }
    }

}
