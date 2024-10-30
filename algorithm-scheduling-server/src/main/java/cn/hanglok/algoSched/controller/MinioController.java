package cn.hanglok.algoSched.controller;

import cn.hanglok.algoSched.entity.res.Res;
import cn.hanglok.algoSched.service.MinioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

/**
 * @author Allen
 * @version 1.0
 * @className MinioController
 * @description TODO
 * @date 2024/10/29
 */
@RestController
@RequestMapping("/minio")
@Tag(name = "3.0 文件存储模块")
public class MinioController {

    @Autowired
    MinioService minioService;

    @GetMapping("/generatePreSignedUrl")
    @Parameters({
            @Parameter(name = "objectName", description = "预上传文件名", in = ParameterIn.QUERY),
    })
    @Operation(summary = "生成上传的预签名 URL")
    public Res<Map<String, String>> generatePreSignedUrl(@RequestParam(value = "objectName") String objectName) {
        String id = UUID.randomUUID().toString();
        String url = minioService.generatePreSignedUrl(String.format("%s/%s", id, objectName));
        return Res.ok(Map.of(
                "id", id,
                "url", url
        ));
    }

}
