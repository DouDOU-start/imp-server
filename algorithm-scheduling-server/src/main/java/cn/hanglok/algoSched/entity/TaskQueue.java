package cn.hanglok.algoSched.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Allen
 * @version 1.0
 * @className Task
 * @description TODO
 * @date 2023/12/28
 */
@Data
public class TaskQueue {

    public static Map<String, Field> value = new HashMap<>();

    @Data
    @AllArgsConstructor
    public static class Field {
        private String taskId;
        private String status;
        private String objectShareUrl;
        private String executionTime;
    }

}
