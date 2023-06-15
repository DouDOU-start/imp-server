package cn.hanglok.dto;

import cn.hanglok.entity.InstitutionPatient;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @author Allen
 * @version 1.0
 * @className Patient
 * @description TODO
 * @date 2023/6/5 17:29
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class InstitutionPatientDto extends InstitutionPatient {
    public InstitutionPatientDto() {
        setCreatedAt(LocalDateTime.now());
        setUpdatedAt(LocalDateTime.now());
    }
}
