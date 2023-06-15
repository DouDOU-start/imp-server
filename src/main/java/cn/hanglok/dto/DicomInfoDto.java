package cn.hanglok.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;
import org.dcm4che3.data.Attributes;

/**
 * @author Allen
 * @version 1.0
 * @className DicomInfo
 * @description TODO
 * @date 2023/6/5 17:25
 */
@Data
@ToString
public class DicomInfoDto {
    @JsonIgnore
    private Attributes attr;
    private InstitutionDto institution;
    private InstitutionPatientDto institutionPatient;
    private ImageStudiesDto imageStudies;
}
