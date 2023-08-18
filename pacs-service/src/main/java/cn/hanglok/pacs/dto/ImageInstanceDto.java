package cn.hanglok.pacs.dto;

import cn.hanglok.pacs.entity.ImageInstances;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Allen
 * @version 1.0
 * @className ImageInstanceDto
 * @description TODO
 * @date 2023/6/8 9:58
 */
@Data
public class ImageInstanceDto extends ImageInstances {
    public ImageInstanceDto() {
        setCreatedAt(LocalDateTime.now());
        setUpdatedAt(LocalDateTime.now());
    }
}
