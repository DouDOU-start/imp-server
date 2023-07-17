package cn.hanglok.controller;

import cn.hanglok.config.AuthorConfig;
import cn.hanglok.dto.BodyPartDto;
import cn.hanglok.dto.HumanOrganDto;
import cn.hanglok.dto.ImageScanTypeDto;
import cn.hanglok.dto.InstitutionDto;
import cn.hanglok.entity.BodyPart;
import cn.hanglok.entity.HumanOrgan;
import cn.hanglok.entity.ImageScanType;
import cn.hanglok.entity.Institution;
import cn.hanglok.entity.res.Res;
import cn.hanglok.service.*;
import cn.hanglok.util.ConvertUtils;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
    IInstitutionService institutionService;

    @Autowired
    IBodyPartService bodyPartService;

    @Autowired
    IHumanOrganService humanOrganService;

    @Autowired
    IImageScanTypeService imageScanTypeService;

    @Autowired
    IDictionaryService dictionaryService;

    @ApiOperationSupport(author = AuthorConfig.AUTHOR_INFO)
    @Operation(summary = "获取机构信息列表")
    @GetMapping("/institution")
    public Res<List<InstitutionDto>> getInstitution() {
        return Res.ok(new ArrayList<>() {{
            institutionService.getInstitutionList().forEach(entity -> add(ConvertUtils.entityToDto(entity, Institution.class, InstitutionDto.class)));
        }});
    }

    @ApiOperationSupport(author = AuthorConfig.AUTHOR_INFO)
    @Operation(summary = "获取模态信息列表")
    @GetMapping("/modality")
    public Res<List<String>> getModality() {
        return Res.ok(dictionaryService.getModality());
    }

    @ApiOperationSupport(author = AuthorConfig.AUTHOR_INFO)
    @Operation(summary = "新增身体检查部位")
    @PostMapping("/bodyPart")
    public Res<Integer> addBodyPart(@RequestBody BodyPartDto bodyPart) {
        return Res.ok(bodyPartService.addBodyPart(bodyPart));
    }

    @ApiOperationSupport(author = AuthorConfig.AUTHOR_INFO)
    @Operation(summary = "获取身体检查部位")
    @GetMapping("/bodyPart")
    public Res<List<BodyPart>> getBodyPart() {
        return Res.ok(bodyPartService.list());
    }

    @ApiOperationSupport(author = AuthorConfig.AUTHOR_INFO)
    @Operation(summary = "修改身体检查部位")
    @PutMapping("/bodyPart")
    public Res<Integer> modifyBodyPart(@RequestBody BodyPartDto bodyPart) {
        return Res.ok(bodyPartService.modifyBodyPart(bodyPart));
    }

    @ApiOperationSupport(author = AuthorConfig.AUTHOR_INFO)
    @Parameters(@Parameter(name = "id", description = "身体检查部位 id", in = ParameterIn.PATH))
    @Operation(summary = "删除身体检查部位")
    @DeleteMapping("/bodyPart/{id}")
    public Res<Integer> delBodyPart(@PathVariable long id) {
        return Res.ok(bodyPartService.delBodyPart(id));
    }

    @ApiOperationSupport(author = AuthorConfig.AUTHOR_INFO)
    @Operation(summary = "新增器官")
    @PostMapping("/humanOrgan")
    public Res<Integer> addHumanOrgan(@RequestBody HumanOrganDto humanOrgan) {
        return Res.ok(humanOrganService.addHumanOrgan(humanOrgan));
    }

    @ApiOperationSupport(author = AuthorConfig.AUTHOR_INFO)
    @Operation(summary = "获取器官")
    @GetMapping("/humanOrgan")
    public Res<List<HumanOrgan>> getHumanOrgan() {
        return Res.ok(humanOrganService.list());
    }

    @ApiOperationSupport(author = AuthorConfig.AUTHOR_INFO)
    @Operation(summary = "修改器官")
    @PutMapping("/humanOrgan")
    public Res<Integer> modifyHumanOrgan(@RequestBody HumanOrganDto humanOrgan) {
        return Res.ok(humanOrganService.modifyHumanOrgan(humanOrgan));
    }

    @ApiOperationSupport(author = AuthorConfig.AUTHOR_INFO)
    @Parameters(@Parameter(name = "id", description = "器官 id", in = ParameterIn.PATH))
    @Operation(summary = "删除器官")
    @DeleteMapping("/humanOrgan/{id}")
    public Res<Integer> delHumanOrgan(@PathVariable long id) {
        return Res.ok(humanOrganService.delHumanOrgan(id));
    }

    @ApiOperationSupport(author = AuthorConfig.AUTHOR_INFO)
    @Operation(summary = "新增扫描类型")
    @PostMapping("/scanType")
    public Res<Integer> addScanType(@RequestBody ImageScanTypeDto imageScanType) {
        return Res.ok(imageScanTypeService.addScanType(imageScanType));
    }

    @ApiOperationSupport(author = AuthorConfig.AUTHOR_INFO)
    @Operation(summary = "获取扫描类型")
    @GetMapping("/scanType")
    public Res<List<ImageScanType>> getScanType() {
        return Res.ok(imageScanTypeService.list());
    }

    @ApiOperationSupport(author = AuthorConfig.AUTHOR_INFO)
    @Operation(summary = "修改扫描类型")
    @PutMapping("/scanType")
    public Res<Integer> modifyScanType(@RequestBody ImageScanTypeDto imageScanType) {
        return Res.ok(imageScanTypeService.modifyScanType(imageScanType));
    }

    @ApiOperationSupport(author = AuthorConfig.AUTHOR_INFO)
    @Parameters(@Parameter(name = "id", description = "扫描类型 id", in = ParameterIn.PATH))
    @Operation(summary = "删除扫描类型")
    @DeleteMapping("/scanType/{id}")
    public Res<Integer> delScanType(@PathVariable long id) {
        return Res.ok(imageScanTypeService.delScanType(id));
    }

}
