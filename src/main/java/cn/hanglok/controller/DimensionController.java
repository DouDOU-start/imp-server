package cn.hanglok.controller;

import cn.hanglok.config.AuthorConfig;
import cn.hanglok.dto.BodyPartDto;
import cn.hanglok.entity.BodyPart;
import cn.hanglok.service.IBodyPartService;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Allen
 * @version 1.0
 * @className DimensionController
 * @description 纬度信息控制器
 * @date 2023/6/19 10:22
 */
@Tag(name = "3.0 纬度信息模块")
@RestController
@RequestMapping("/dimension")
public class DimensionController {

    @Autowired
    IBodyPartService bodyPartService;

    @ApiOperationSupport(author = AuthorConfig.AUTHOR_INFO)
    @Operation(summary = "新增身体检查部位")
    @PostMapping("/bodyPart")
    public int AddBodyPart(@RequestBody BodyPartDto bodyPart) {
        return bodyPartService.AddBodyPart(bodyPart);
    }

    @ApiOperationSupport(author = AuthorConfig.AUTHOR_INFO)
    @Operation(summary = "获取身体检查部位")
    @GetMapping("/bodyPart")
    public List<BodyPart> getBodyPart() {
        return bodyPartService.list();
    }

    @ApiOperationSupport(author = AuthorConfig.AUTHOR_INFO)
    @Operation(summary = "修改身体检查部位")
    @PutMapping("/bodyPart")
    public int modifyBodyPart(@RequestBody BodyPartDto bodyPart) {
        return bodyPartService.modifyBodyPart(bodyPart);
    }

    @ApiOperationSupport(author = AuthorConfig.AUTHOR_INFO)
    @Parameters(@Parameter(name = "id", description = "身体检查部位 id", in = ParameterIn.PATH))
    @Operation(summary = "删除身体检查部位")
    @DeleteMapping("/bodyPart/{id}")
    public int delBodyPart(@PathVariable long id) {
        return bodyPartService.delBodyPart(id);
    }
}
