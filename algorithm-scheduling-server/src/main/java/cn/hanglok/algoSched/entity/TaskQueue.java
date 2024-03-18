package cn.hanglok.algoSched.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Allen
 * @version 1.0
 * @className Task
 * @description TODO
 * @date 2023/12/28
 */
@Data
public class TaskQueue {

    public static Map<String, Field> value = new ConcurrentHashMap<>();

    @Data
    @AllArgsConstructor
    public static class Field {
        private String taskId;
        private String status;
        private String objectShareUrl;
        private String executionTime;
        private String errMsg;
    }

}
