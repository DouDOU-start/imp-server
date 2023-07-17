package cn.hanglok.dto;

import cn.hanglok.entity.ImageSeries;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @author Allen
 * @version 1.0
 * @className Series
 * @description TODO
 * @date 2023/6/5 17:33
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ImageSeriesDto extends ImageSeries {
    private ImageInstanceDto imageInstance;

    public ImageSeriesDto() {
        setCreatedAt(LocalDateTime.now());
        setUpdatedAt(LocalDateTime.now());
    }
}
