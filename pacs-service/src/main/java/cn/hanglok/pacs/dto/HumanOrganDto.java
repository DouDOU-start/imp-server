package cn.hanglok.pacs.dto;

import cn.hanglok.pacs.entity.HumanOrgan;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;

/**
 * @author Allen
 * @version 1.0
 * @className HumanOrganDto
 * @description HumanOrganDto
 * @date 2023/6/28 14:44
 */
public class HumanOrganDto extends HumanOrgan {

    @JsonIgnore
    private LocalDateTime createdAt;

    @JsonIgnore
    private LocalDateTime updatedAt;

    @JsonIgnore
    private Long creator;

    @JsonIgnore
    private Long updater;

    public HumanOrganDto() {
        setCreatedAt(LocalDateTime.now());
        setUpdatedAt(LocalDateTime.now());
    }
}
