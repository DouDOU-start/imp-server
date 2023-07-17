package cn.hanglok.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @author Allen
 * @version 1.0
 * @className ModifySeriesBodyPartDto
 * @description TODO
 * @date 2023/7/13 16:27
 */
@Data
public class ModifySeriesBodyPartDto {
    @Schema(description = "系列 id")
    private Long seriesId;

    @Schema(description = "操作")
    private List<SeriesBodyPartOP> operates;

    @Data
    public static class SeriesBodyPartOP {
        private Operate op;
        @Schema(description = "身体检查部位 id")
        private String bodyPartId;
    }
}
