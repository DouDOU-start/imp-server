package cn.hanglok.algoSched.controller;

import cn.hanglok.algoSched.entity.res.Res;
import cn.hanglok.algoSched.entity.TaskQueue;
import cn.hanglok.algoSched.service.DockerService;
import cn.hanglok.algoSched.service.MinioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

/**
 * @author Allen
 * @version 1.0
 * @className AlgorithmController
 * @description 算法模块
 * @date 2023/9/20
 */
@RestController
@RequestMapping("/algorithm")
@Tag(name = "1.0 算法模块")
public class AlgorithmController {

    @Autowired
    MinioService minioService;

    @Autowired
    DockerService dockerService;

    /**
     * 上传影像文件并执行对应分割算法
     * @param file 影像文件
     * @throws Exception 异常
     */
    @PostMapping("/execute")
    @Operation(summary = "执行算法分割")
    public Res executeAlgorithm(@RequestParam(value = "file") MultipartFile file) {

        String taskId = UUID.randomUUID().toString();

        TaskQueue.value.put(taskId, new TaskQueue.Field(taskId,"running", null, null));

        minioService.uploadFile(file, String.format("/input/%s/", taskId));

        String objectUrl = minioService.getObjectUrl(String.format("/input/%s/", taskId) + file.getOriginalFilename(), 60 * 30);

        dockerService.executeLungSegmentation(taskId, objectUrl);

        return Res.ok(TaskQueue.value.get(taskId));
    }

    @GetMapping("/{taskId}")
    @Operation(summary = "查询算法分割结果")
    public Res getExecuteStatus(@PathVariable String taskId) {
        return Res.ok(TaskQueue.value.get(taskId));
    }


}
