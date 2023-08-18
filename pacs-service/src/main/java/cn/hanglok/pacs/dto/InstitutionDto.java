package cn.hanglok.pacs.dto;

import cn.hanglok.pacs.entity.Institution;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @author Allen
 * @version 1.0
 * @className Institution
 * @description TODO
 * @date 2023/6/5 17:29
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class InstitutionDto extends Institution {
    public InstitutionDto() {
        setCreatedAt(LocalDateTime.now());
        setUpdatedAt(LocalDateTime.now());
    }
}
