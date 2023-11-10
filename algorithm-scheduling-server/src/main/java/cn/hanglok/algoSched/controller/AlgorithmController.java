package cn.hanglok.algoSched.controller;

import cn.hanglok.algoSched.entity.res.Res;
import cn.hanglok.algoSched.socket.AlgorithmServerHandler;
import cn.hanglok.algoSched.util.MinioUtil;
import com.alibaba.fastjson.JSONObject;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    MinioUtil minioUtil;

    @GetMapping
    @Operation(summary = "获取算法列表")
    public Object getAlgorithmList() {
        return AlgorithmServerHandler.ctxMap;
    }

    /**
     * 上传影像文件并执行对应分割算法
     * @param pictureFile 影像文件
     * @param algorithmId 算法 id
     * @throws Exception 异常
     */
    @PostMapping("/execute/{algorithmId}")
    @Operation(summary = "执行算法分割")
    @Parameters({
            @Parameter(name = "algorithmId", description = "算法 id", in = ParameterIn.PATH)
    })
    public Res<String> executeAlgorithm(@RequestParam(value = "pictureFile") MultipartFile pictureFile,
                                        @PathVariable(value = "algorithmId") String algorithmId) throws Exception {
        AlgorithmServerHandler.Ctx ctx = AlgorithmServerHandler.ctxMap.get(algorithmId);
        ctx.getCtx().writeAndFlush(
                Unpooled.copiedBuffer(
                        new JSONObject() {{
                            put("msgType", "req");
                            put("msgContent", new JSONObject() {{
                                put("pictureFileUrl", minioUtil.uploadFile(pictureFile, "tmp"));
                            }});
                        }}.toString(),
                        CharsetUtil.UTF_8)
        );
        return Res.ok("success");
    }

    @GetMapping("/url")
    public String getUrl() throws Exception {
        return minioUtil.getObjectUrl("tmp", "20230920_155421_8913.gz", 60);
    }
}
