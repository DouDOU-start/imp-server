package cn.hanglok.algoSched.controller;

import cn.hanglok.algoSched.component.AlgorithmTaskExecutor;
import cn.hanglok.algoSched.entity.Assembles;
import cn.hanglok.algoSched.entity.Images;
import cn.hanglok.algoSched.entity.TaskQueue;
import cn.hanglok.algoSched.entity.Template;
import cn.hanglok.algoSched.entity.res.Res;
import cn.hanglok.algoSched.entity.res.ResCode;
import cn.hanglok.algoSched.service.IAssemblesService;
import cn.hanglok.algoSched.service.IImagesService;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

/**
 * @author Allen
 * @version 1.0
 * @className TemplateController
 * @description TODO
 * @date 2024/3/1
 */
@RestController
@RequestMapping("/assemble")
@Tag(name = "2.0 组合模板模块")
public class TemplateController {

    @Autowired
    IImagesService imagesService;

    @Autowired
    IAssemblesService assemblesService;

    @Autowired
    AlgorithmTaskExecutor algorithmTaskExecutor;

    @GetMapping("/images")
    @Operation(summary = "获取算法镜像列表")
    public Res<List<Images>> getImages() {
        return Res.ok(imagesService.list());
    }

    @GetMapping()
    @Operation(summary = "获取模板列表")
    public Res<List<Assembles>> getAssembles() {
        return Res.ok(assemblesService.list());
    }

    @PostMapping()
    @Parameters({
            @Parameter(name = "assembleName", description = "模板名", in = ParameterIn.QUERY),
    })
    @Operation(summary = "新增模板")
    public Res addAssemble(@RequestParam(value = "assembleName") String assembleName,
                           @RequestBody Template template) {
        return Res.ok(assemblesService.save(
                new Assembles() {{
                    setName(assembleName);
                    setData(JSON.toJSONString(template));
                }}
        ));
    }

    @PutMapping()
    @Parameters({
            @Parameter(name = "assembleName", description = "模板名", in = ParameterIn.QUERY),
    })
    @Operation(summary = "更新模板")
    public Res updateAssemble(@RequestParam(value = "assembleName") String assembleName,
                           @RequestBody Template template) {
        UpdateWrapper<Assembles> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("name", assembleName).set("data", JSON.toJSONString(template));
        return Res.ok(assemblesService.update(updateWrapper));
    }

    @DeleteMapping()
    @Parameters({
            @Parameter(name = "assembleName", description = "模板名", in = ParameterIn.QUERY),
    })
    @Operation(summary = "删除模板")
    public Res deleteAssemble(@RequestParam(value = "assembleName") String assembleName) {
        QueryWrapper<Assembles> wrapper = new QueryWrapper<Assembles>() {{
            eq("name", assembleName);
        }};
        return Res.ok(assemblesService.remove(wrapper));
    }

    @PostMapping("/execute")
    @Parameters({
            @Parameter(name = "assembleName", description = "模板名", in = ParameterIn.QUERY),
    })
    @Operation(summary = "执行算法模板")
    public Res execute(@RequestParam(value = "assembleName") String assembleName,
                       @RequestParam(value = "file") MultipartFile file) throws JsonProcessingException {
        String taskId = UUID.randomUUID().toString();

        return algorithmTaskExecutor.execute(taskId, assembleName, file) ? Res.ok(TaskQueue.value.get(taskId)) : Res.error(ResCode.BUSY);
    }
}
