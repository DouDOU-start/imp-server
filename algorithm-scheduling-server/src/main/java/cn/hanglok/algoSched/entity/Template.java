package cn.hanglok.algoSched.entity;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
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
        private String version;
        private List<Map<String, Object>> inputFile;
        private String outputFile;
        private String gpu;
        private AlgorithmModel child;
    }

    public static String createExecEnvJson(String taskId, String inputFile, AlgorithmModel algorithmModel) {
        return new JSONObject() {{
            put("task_id", taskId);
            if (algorithmModel.getInputFile().isEmpty()) {
                put("input", new ArrayList<>() {{
                    add(new HashMap<>() {{
                        put("object_name", inputFile);
                    }});
                }});
            } else {

                List<Object> newInputFile = new ArrayList<>();

                algorithmModel.getInputFile().forEach(i -> {
                    newInputFile.add(new HashMap<>() {{
                        put("object_name", i.get("file_name").toString().replace("%s", taskId));
                        put("label", i.get("label"));
                    }});
                });
                put("input", newInputFile);
            }
            put("output", algorithmModel.getOutputFile());
        }}.toString();
    }
}
