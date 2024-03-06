package cn.hanglok.algoSched.entity;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import java.util.List;
import java.util.Map;

/**
 * @author Allen
 * @version 1.0
 * @className Template
 * @description TODO
 * @date 2024/1/18
 */
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
    }

    public static String createExecEnvJson(String taskId, AlgorithmModel algorithmModel) {
        return new JSONObject() {{
            put("task_id", taskId);
            put("args", algorithmModel.getArgs());
            put("inputFile", algorithmModel.getInputFile());
            put("outputFile", algorithmModel.getOutputFile());
        }}.toString();
    }
}
