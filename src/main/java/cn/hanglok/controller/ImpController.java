package cn.hanglok.controller;

import cn.hanglok.config.AuthorConfig;
import cn.hanglok.dto.*;
import cn.hanglok.entity.ImageInstances;
import cn.hanglok.entity.ImageLabel;
import cn.hanglok.entity.res.Res;
import cn.hanglok.service.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
 * @className ImpController
 * @description 影像模块控制器类
 * @date 2023/6/9 16:35
 */
@Tag(name = "1.0 影像模块")
@RestController
@RequestMapping("/imp")
public class ImpController {

    @Autowired
    IImageSeriesService imageSeriesService;

    @Autowired
    ILabelOrganService labelOrganService;

    @Autowired
    ISeriesBodyPartService seriesBodyPartService;

    @Autowired
    ISeriesScanTypeService seriesScanTypeService;

    @Autowired
    IImageInstancesService imageInstancesService;

    @Autowired
    IImageLabelService imageLabelService;

    @ApiOperationSupport(author = AuthorConfig.AUTHOR_INFO)
    @Operation(summary = "获取系列简要信息列表")
    @Parameters({
            @Parameter(name = "keyword", description = "关键字", in = ParameterIn.QUERY),
            @Parameter(name = "institutionIds", description = "机构ID（可多选）", in = ParameterIn.QUERY, example = "2,3,15"),
            @Parameter(name = "modality", description = "模态过滤(可多选)", in = ParameterIn.QUERY, example = "CT,MRI"),
            @Parameter(name = "sliceRange", description = "切片厚度范围", in = ParameterIn.QUERY, example = "0.625,3"),
            @Parameter(name = "bodyPartIds", description = "身体检查部位ID(可多选)", in = ParameterIn.QUERY, example = "2,3,15"),
            @Parameter(name = "patientSex", description = "患者性别过滤，男：`M`，女：`F`", in = ParameterIn.QUERY),
            @Parameter(name = "organIds",description = "器官ID(可多选)", in = ParameterIn.QUERY, example = "2,3,15"),
            @Parameter(name = "scanTypeIds",description = "扫描类型ID(可多选)", in = ParameterIn.QUERY, example = "2,3,15"),
            @Parameter(name = "currentPage",description = "分页当前页", in = ParameterIn.QUERY),
            @Parameter(name = "pageSize",description = "页面大小", in = ParameterIn.QUERY),
    })
    @GetMapping("/series")
    public Res<IPage<SimpleSeriesOutDto>> getSimpleSeriesList(@RequestParam(required = false) String keyword,
                                                              @RequestParam(required = false) Long[] institutionIds,
                                                              @RequestParam(required = false) String[] modality,
                                                              @RequestParam(required = false) Double[] sliceRange,
                                                              @RequestParam(required = false) Long[] bodyPartIds,
                                                              @RequestParam(required = false) String patientSex,
                                                              @RequestParam(required = false) Long[] organIds,
                                                              @RequestParam(required = false) Long[] scanTypeIds,
                                                              @RequestParam int currentPage,
                                                              @RequestParam int pageSize) {

        return Res.ok(imageSeriesService.getSimpleSeriesList(
                keyword,
                institutionIds,
                modality,
                sliceRange,
                bodyPartIds,
                patientSex,
                organIds,
                scanTypeIds,
                currentPage,
                pageSize
        ));

    }

    @ApiOperationSupport(author = AuthorConfig.AUTHOR_INFO)
    @Operation(summary = "获取实例信息列表")
    @GetMapping("/instance/{seriesId}")
    public Res<List<ImageInstances>> getSeriesInstance(@PathVariable String seriesId) {
        return Res.ok(imageInstancesService.getInstances(seriesId));
    }

    @ApiOperationSupport(author = AuthorConfig.AUTHOR_INFO)
    @Operation(summary = "获取系列详情")
    @GetMapping("/series/{seriesId}")
    public Res<SeriesDetailOutDto> getSeriesDetail(@PathVariable String seriesId) {
        return Res.ok(imageSeriesService.getSeriesDetail(seriesId));
    }

    @ApiOperationSupport(author = AuthorConfig.AUTHOR_INFO)
    @Operation(summary = "修改标签器官")
    @PutMapping("/labelOrgan")
    public Res<Integer> modifyLabelOrgan(@RequestBody ModifyLabelOrganDto modifyLabelOrganDto) {
        return Res.ok(labelOrganService.modifyLabelOrgan(modifyLabelOrganDto));
    }

    @ApiOperationSupport(author = AuthorConfig.AUTHOR_INFO)
    @Operation(summary = "修改系列身体检查部位")
    @PutMapping("/bodyPart")
    public Res<Integer> modifyBodyPart(@RequestBody ModifySeriesBodyPartDto modifyLabelOrganDto) {
        return Res.ok(seriesBodyPartService.modifyBodyPart(modifyLabelOrganDto));
    }

    @ApiOperationSupport(author = AuthorConfig.AUTHOR_INFO)
    @Operation(summary = "修改系列扫描类型")
    @PutMapping("/scanType")
    public Res<Integer> modifyScanType(@RequestBody ModifySeriesScanTypeDto modifySeriesScanTypeDto) {
        return Res.ok(seriesScanTypeService.modifyScanType(modifySeriesScanTypeDto));
    }

    @ApiOperationSupport(author = AuthorConfig.AUTHOR_INFO)
    @Operation(summary = "获取标签信息列表")
    @GetMapping("/label/{seriesId}")
    public Res<List<ImageLabel>> getSeriesLabel(@PathVariable String seriesId) {
        return Res.ok(imageLabelService.list(new QueryWrapper<>() {{
            eq("series_id", seriesId);
        }}));
    }

}
