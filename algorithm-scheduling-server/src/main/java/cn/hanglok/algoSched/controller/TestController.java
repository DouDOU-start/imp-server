package cn.hanglok.algoSched.controller;

import cn.hanglok.algoSched.component.AlgorithmAssembleMonitor;
import cn.hanglok.algoSched.component.AlgorithmExecutor;
import cn.hanglok.algoSched.component.HanglokAlgorithm;
import cn.hanglok.algoSched.dto.AlgorithmCallback;
import cn.hanglok.algoSched.entity.res.Res;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Allen
 * @version 1.0
 * @className TestController
 * @description TODO
 * @date 2024/3/14
 */
@RestController
@RequestMapping("/test")
@Tag(name = "3.0 测试模块")
public class TestController {
    
    
    @Autowired
    AlgorithmExecutor algorithmExecutor;

    @Autowired
    HanglokAlgorithm hanglokAlgorithm;

    @PostMapping("/execute")
    public Res execute(@RequestParam(value = "assembleName") String assembleName) throws JsonProcessingException {
//        String taskId = UUID.randomUUID().toString();
        String taskId = "888888";
        algorithmExecutor.execute(taskId, assembleName);
        return Res.ok(taskId);
    }

    @GetMapping("getStatus")
    public void getStatus() {
        String taskId = "888888";
        hanglokAlgorithm.getAlgorithmAssembleMonitor(taskId);
    }

    @PostMapping("/callback")
    public void callback(@RequestBody AlgorithmCallback algorithmCallback) {
        AlgorithmAssembleMonitor algorithmAssembleMonitor = hanglokAlgorithm.getAlgorithmAssembleMonitor(algorithmCallback.getTaskId());
        algorithmAssembleMonitor.taskCallback(algorithmCallback);
    }
}
