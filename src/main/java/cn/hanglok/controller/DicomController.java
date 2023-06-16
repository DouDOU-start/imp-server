package cn.hanglok.controller;

import cn.hanglok.config.AuthorConfig;
import cn.hanglok.dto.InstitutionDto;
import cn.hanglok.dto.SimpleSeriesOutDto;
import cn.hanglok.entity.Institution;
import cn.hanglok.service.IImageSeriesService;
import cn.hanglok.service.IInstitutionService;
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
 * @className DicomController
 * @description Dicom模块控制器类
 * @date 2023/6/9 16:35
 */
@Tag(name = "1.0 DICOM模块")
@RestController
@RequestMapping("/dicom")
public class DicomController {

    @Autowired
    IImageSeriesService iImageSeriesService;

    @Autowired
    IInstitutionService institutionService;

    @ApiOperationSupport(author = AuthorConfig.AUTHOR_INFO)
    @Operation(summary = "获取系列简要信息列表")
    @Parameters({
            @Parameter(name = "keyword", description = "关键字", in = ParameterIn.QUERY),
            @Parameter(name = "institutionIds",description = "机构ID（可多选）", in = ParameterIn.QUERY, example = "2,3,15"),
            @Parameter(name = "modality",description = "模态过滤(可多选)", in = ParameterIn.QUERY, example = "CT,MRI"),
            @Parameter(name = "sliceRange",description = "切片厚度范围", in = ParameterIn.QUERY, example = "0.625,3"),
            @Parameter(name = "bodyPartIds",description = "身体检查部位ID(可多选)", in = ParameterIn.QUERY, example = "2,3,15"),
            @Parameter(name = "patientSex",description = "患者性别过滤，男：`M`，女：`F`", in = ParameterIn.QUERY),
            @Parameter(name = "organIds",description = "器官ID(可多选)", in = ParameterIn.QUERY, example = "2,3,15"),
            @Parameter(name = "scanTypeIds",description = "扫描类型ID(可多选)", in = ParameterIn.QUERY, example = "2,3,15"),
            @Parameter(name = "currentPage",description = "分页当前页", in = ParameterIn.QUERY),
            @Parameter(name = "pageSize",description = "页面大小", in = ParameterIn.QUERY),
    })
    @GetMapping
    public List<SimpleSeriesOutDto> getSimpleSeriesList(@RequestParam(required = false) String keyword,
                                                        @RequestParam(required = false) Long[] institutionIds,
                                                        @RequestParam(required = false) String[] modality,
                                                        @RequestParam(required = false) Double[] sliceRange,
                                                        @RequestParam(required = false) Long[] bodyPartIds,
                                                        @RequestParam(required = false) String patientSex,
                                                        @RequestParam(required = false) Long[] organIds,
                                                        @RequestParam(required = false) Long[] scanTypeIds,
                                                        @RequestParam int currentPage,
                                                        @RequestParam int pageSize) {

        return iImageSeriesService.getSimpleSeriesList(
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
        );

    }

    @ApiOperationSupport(author = AuthorConfig.AUTHOR_INFO)
    @Operation(summary = "获取机构信息列表")
    @GetMapping("/institution")
    public List<InstitutionDto> getInstitution() {
        return new ArrayList<>() {{
            institutionService.getInstitutionList().forEach(entity -> add(ConvertUtils.entityToDto(entity, Institution.class, InstitutionDto.class)));
        }};
    }

    @ApiOperationSupport(author = AuthorConfig.AUTHOR_INFO)
    @Operation(summary = "获取模态信息列表")
    @GetMapping("/modality")
    public List<String> getModality() {
        return iImageSeriesService.getModality();
    }
}
