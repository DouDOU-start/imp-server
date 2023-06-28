package cn.hanglok.dto;

import cn.hanglok.entity.BodyPart;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;

/**
 * @author Allen
 * @version 1.0
 * @className BodyPartDto
 * @description BodyPartDto
 * @date 2023/6/27 17:31
 */
public class BodyPartDto extends BodyPart {

    @JsonIgnore
    private LocalDateTime createdAt;

    @JsonIgnore
    private LocalDateTime updatedAt;

    @JsonIgnore
    private Long creator;

    @JsonIgnore
    private Long updater;


    public BodyPartDto() {
        setCreatedAt(LocalDateTime.now());
        setUpdatedAt(LocalDateTime.now());
    }
}
