package cn.hanglok.pacs.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @author Allen
 * @version 1.0
 * @className ModifySeriesScanTypeDto
 * @description TODO
 * @date 2023/7/17 13:49
 */
@Data
public class ModifySeriesScanTypeDto {
    @Schema(description = "系列 id")
    private Long seriesId;

    @Schema(description = "操作")
    private List<SeriesScanTypeOP> operates;

    @Data
    public static class SeriesScanTypeOP {
        private Operate op;
        @Schema(description = "扫描类型 id")
        private String scanTypeId;
    }
}
