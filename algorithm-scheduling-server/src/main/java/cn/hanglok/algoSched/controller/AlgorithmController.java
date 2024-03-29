package cn.hanglok.algoSched.controller;

import cn.hanglok.algoSched.annotation.RequireValidToken;
import cn.hanglok.algoSched.component.AlgorithmExecutor;
import cn.hanglok.algoSched.entity.Assembles;
import cn.hanglok.algoSched.entity.TaskStdout;
import cn.hanglok.algoSched.entity.Template;
import cn.hanglok.algoSched.entity.res.Res;
import cn.hanglok.algoSched.entity.TaskQueue;
import cn.hanglok.algoSched.exception.TemplateErrorException;
import cn.hanglok.algoSched.service.IAssemblesService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
    IAssemblesService assemblesService;

    @Autowired
    AlgorithmExecutor algorithmExecutor;

    /**
     * 上传影像文件并执行对应分割算法
     * @param file 影像文件
     */
    @PostMapping("/execute")
    @Operation(summary = "执行算法分割")
    @RequireValidToken
    public Res executeAlgorithm(@RequestParam(value = "file") MultipartFile file) {

        String taskId = UUID.randomUUID().toString();

        Assembles as = assemblesService.getOne(new QueryWrapper<>() {{
            eq("name", "lungsegmentation");
        }});

        if (null == as) {
            throw new TemplateErrorException("Not found the corresponding template: lungsegmentation");
        }

        algorithmExecutor.execute(taskId, Template.load(as.getData()), file);

        return Res.ok(TaskQueue.value.get(taskId));
    }

    @GetMapping("/{taskId}")
    @Operation(summary = "查询算法分割结果")
    @RequireValidToken
    public Res getExecuteStatus(@PathVariable String taskId) {
        return Res.ok(TaskQueue.value.get(taskId));
    }

    @GetMapping
    @Operation(summary = "查询任务列表")
    @RequireValidToken
    public Res getTaskList() {
        return Res.ok(TaskQueue.value);
    }

    @GetMapping("/log/{taskId}")
    @Operation(summary = "查询任务日志")
    @RequireValidToken
    public Res getTaskLog(@PathVariable String taskId) {
        return Res.ok(TaskStdout.value.get(taskId));
    }

}
