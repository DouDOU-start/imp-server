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
    IImageSeriesService imageSeriesService;

    @Autowired
    IBodyPartService bodyPartService;

    @Autowired
    IHumanOrganService humanOrganService;

    @Autowired
    IImageScanTypeService imageScanTypeService;

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
    public List<String> getModality() {
        return imageSeriesService.getModality();
    }

    @ApiOperationSupport(author = AuthorConfig.AUTHOR_INFO)
    @Operation(summary = "新增身体检查部位")
    @PostMapping("/bodyPart")
    public int addBodyPart(@RequestBody BodyPartDto bodyPart) {
        return bodyPartService.addBodyPart(bodyPart);
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

    @ApiOperationSupport(author = AuthorConfig.AUTHOR_INFO)
    @Operation(summary = "新增器官")
    @PostMapping("/humanOrgan")
    public int addHumanOrgan(@RequestBody HumanOrganDto humanOrgan) {
        return humanOrganService.addHumanOrgan(humanOrgan);
    }

    @ApiOperationSupport(author = AuthorConfig.AUTHOR_INFO)
    @Operation(summary = "获取器官")
    @GetMapping("/humanOrgan")
    public List<HumanOrgan> getHumanOrgan() {
        return humanOrganService.list();
    }

    @ApiOperationSupport(author = AuthorConfig.AUTHOR_INFO)
    @Operation(summary = "修改器官")
    @PutMapping("/humanOrgan")
    public int modifyHumanOrgan(@RequestBody HumanOrganDto humanOrgan) {
        return humanOrganService.modifyHumanOrgan(humanOrgan);
    }

    @ApiOperationSupport(author = AuthorConfig.AUTHOR_INFO)
    @Parameters(@Parameter(name = "id", description = "器官 id", in = ParameterIn.PATH))
    @Operation(summary = "删除器官")
    @DeleteMapping("/humanOrgan/{id}")
    public int delHumanOrgan(@PathVariable long id) {
        return humanOrganService.delHumanOrgan(id);
    }

    @ApiOperationSupport(author = AuthorConfig.AUTHOR_INFO)
    @Operation(summary = "新增扫描类型")
    @PostMapping("/scanType")
    public int addScanType(@RequestBody ImageScanTypeDto imageScanType) {
        return imageScanTypeService.addScanType(imageScanType);
    }

    @ApiOperationSupport(author = AuthorConfig.AUTHOR_INFO)
    @Operation(summary = "获取扫描类型")
    @GetMapping("/scanType")
    public List<ImageScanType> getScanType() {
        return imageScanTypeService.list();
    }

    @ApiOperationSupport(author = AuthorConfig.AUTHOR_INFO)
    @Operation(summary = "修改扫描类型")
    @PutMapping("/scanType")
    public int modifyScanType(@RequestBody ImageScanTypeDto imageScanType) {
        return imageScanTypeService.modifyScanType(imageScanType);
    }

    @ApiOperationSupport(author = AuthorConfig.AUTHOR_INFO)
    @Parameters(@Parameter(name = "id", description = "扫描类型 id", in = ParameterIn.PATH))
    @Operation(summary = "删除扫描类型")
    @DeleteMapping("/scanType/{id}")
    public int delScanType(@PathVariable long id) {
        return imageScanTypeService.delScanType(id);
    }

}
