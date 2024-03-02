package cn.hanglok.algoSched.controller;

import cn.hanglok.algoSched.entity.Images;
import cn.hanglok.algoSched.entity.res.Res;
import cn.hanglok.algoSched.service.IImagesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Allen
 * @version 1.0
 * @className TemplateController
 * @description TODO
 * @date 2024/3/1
 */
@RestController
@RequestMapping("/model")
@Tag(name = "2.0 模板组合模块")
public class TemplateController {

    @Autowired
    IImagesService imagesService;

    @GetMapping("/images")
    @Operation(summary = "获取算法镜像列表")
    public Res<List<Images>> getImages() {
        return Res.ok(imagesService.list());
    }
}
