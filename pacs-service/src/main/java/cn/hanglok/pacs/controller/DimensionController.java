package cn.hanglok.pacs.controller;

import cn.hanglok.pacs.config.AuthorConfig;
import cn.hanglok.pacs.dto.BodyPartDto;
import cn.hanglok.pacs.dto.HumanOrganDto;
import cn.hanglok.pacs.dto.ImageScanTypeDto;
import cn.hanglok.pacs.entity.BodyPart;
import cn.hanglok.pacs.entity.HumanOrgan;
import cn.hanglok.pacs.entity.ImageScanType;
import cn.hanglok.pacs.entity.Institution;
import cn.hanglok.pacs.entity.res.Res;
import cn.hanglok.pacs.service.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
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
    @Parameters({
            @Parameter(name = "keyword",description = "关键字", in = ParameterIn.QUERY),
            @Parameter(name = "currentPage",description = "分页当前页", in = ParameterIn.QUERY),
            @Parameter(name = "pageSize",description = "页面大小", in = ParameterIn.QUERY),
    })
    @GetMapping("/institution")
    public Res<IPage<Institution>> getInstitution(@RequestParam(required = false) String keyword,
                                                     @RequestParam(required = false) Integer currentPage,
                                                     @RequestParam(required = false) Integer pageSize) {
        return Res.ok(institutionService.selectPage(keyword, currentPage, pageSize));
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
    @Parameters({
            @Parameter(name = "keyword",description = "关键字", in = ParameterIn.QUERY),
            @Parameter(name = "currentPage",description = "分页当前页", in = ParameterIn.QUERY),
            @Parameter(name = "pageSize",description = "页面大小", in = ParameterIn.QUERY),
    })
    @GetMapping("/bodyPart")
    public Res<IPage<BodyPart>> getBodyPart(@RequestParam(required = false) String keyword,
                                            @RequestParam(required = false) Integer currentPage,
                                            @RequestParam(required = false) Integer pageSize) {
        return Res.ok(bodyPartService.selectPage(keyword, currentPage, pageSize));
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
    @Parameters({
            @Parameter(name = "keyword",description = "关键字", in = ParameterIn.QUERY),
            @Parameter(name = "currentPage",description = "分页当前页", in = ParameterIn.QUERY),
            @Parameter(name = "pageSize",description = "页面大小", in = ParameterIn.QUERY),
    })
    @GetMapping("/humanOrgan")
    public Res<IPage<HumanOrgan>> getHumanOrgan(@RequestParam(required = false) String keyword,
                                                @RequestParam(required = false) Integer currentPage,
                                                @RequestParam(required = false) Integer pageSize) {
        return Res.ok(humanOrganService.selectPage(keyword, currentPage, pageSize));
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
    public Res<IPage<ImageScanType>> getScanType(@RequestParam(required = false) String keyword,
                                                @RequestParam(required = false) Integer currentPage,
                                                @RequestParam(required = false) Integer pageSize) {
        return Res.ok(imageScanTypeService.selectPage(keyword, currentPage, pageSize));
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
