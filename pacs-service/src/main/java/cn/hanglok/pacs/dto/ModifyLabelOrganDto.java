package cn.hanglok.pacs.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @author Allen
 * @version 1.0
 * @className ModifyLabelOrganDto
 * @description TODO
 * @date 2023/6/29 15:33
 */
@Data
public class ModifyLabelOrganDto {

    @Schema(description = "标签 id")
    private Long labelId;


    @Schema(description = "操作")
    private List<LabelOrganOP> operates;

    @Data
    public static class LabelOrganOP {
        private Operate op;
        @Schema(description = "器官 id")
        private String organId;
    }

}


