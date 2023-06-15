package cn.hanglok.dto;

import cn.hanglok.entity.ImageStudies;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @author Allen
 * @version 1.0
 * @className Studies
 * @description TODO
 * @date 2023/6/5 17:30
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ImageStudiesDto extends ImageStudies {
    private ImageSeriesDto imageSeries;

    public ImageStudiesDto() {
        setCreatedAt(LocalDateTime.now());
        setUpdatedAt(LocalDateTime.now());
    }
}
