package cn.hanglok.algoSched.dto;

import lombok.Data;

/**
 * @author Allen
 * @version 1.0
 * @className AlgorithmCallback
 * @description TODO
 * @date 2024/3/13
 */
@Data
public class AlgorithmCallback {
    private String taskId;
    private String image;
    private Boolean isSuccess;
    private String stdout;
}
