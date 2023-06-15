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
 * @description TODO
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
            @Parameter(name = "institutionId",description = "机构ID", in = ParameterIn.QUERY),
            @Parameter(name = "modality",description = "模态过滤，例如：CT、MRI", in = ParameterIn.QUERY),
            @Parameter(name = "patientSex",description = "患者性别过滤，男：`M`，女：`F`", in = ParameterIn.QUERY),
            @Parameter(name = "sliceRange",description = "切片厚度范围，例如`0,0.05`，`0.05,0.625`，`0.625-0.625`", in = ParameterIn.QUERY),
            @Parameter(name = "currentPage",description = "分页当前页", in = ParameterIn.QUERY),
            @Parameter(name = "pageSize",description = "页面大小", in = ParameterIn.QUERY),
    })
    @GetMapping
    public List<SimpleSeriesOutDto> getSimpleSeriesList(@RequestParam(required = false) Long institutionId,
                                                        @RequestParam(required = false) String modality,
                                                        @RequestParam(required = false) String patientSex,
                                                        @RequestParam(required = false) String sliceRange,
                                                        @RequestParam int currentPage,
                                                        @RequestParam int pageSize) {
        return iImageSeriesService.getSimpleSeriesList(institutionId, modality, patientSex, sliceRange, currentPage, pageSize);
    }

    @ApiOperationSupport(author = AuthorConfig.AUTHOR_INFO)
    @Operation(summary = "获取机构信息列表")
    @GetMapping("/institution")
    public List<InstitutionDto> getInstitution() {
        return new ArrayList<>() {{
            institutionService.getInstitutionList().forEach(entity -> {
                add(ConvertUtils.entityToDto(entity, Institution.class, InstitutionDto.class));
            });
        }};
    }

    @ApiOperationSupport(author = AuthorConfig.AUTHOR_INFO)
    @Operation(summary = "获取模态信息列表")
    @GetMapping("/modality")
    public List<String> getModality() {
        return iImageSeriesService.getModality();
    }
}
