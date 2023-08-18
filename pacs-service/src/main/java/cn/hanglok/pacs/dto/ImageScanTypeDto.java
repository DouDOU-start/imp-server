package cn.hanglok.pacs.dto;

import cn.hanglok.pacs.entity.ImageScanType;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;

/**
 * @author Allen
 * @version 1.0
 * @className ImageScanTypeDto
 * @description ImageScanTypeDto
 * @date 2023/6/28 16:46
 */
public class ImageScanTypeDto extends ImageScanType {
    @JsonIgnore
    private LocalDateTime createdAt;

    @JsonIgnore
    private LocalDateTime updatedAt;

    @JsonIgnore
    private Long creator;

    @JsonIgnore
    private Long updater;

    public ImageScanTypeDto() {
        setCreatedAt(LocalDateTime.now());
        setUpdatedAt(LocalDateTime.now());
    }
}
