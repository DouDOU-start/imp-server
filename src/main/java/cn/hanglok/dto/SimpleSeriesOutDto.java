package cn.hanglok.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Allen
 * @version 1.0
 * @className SimpleDicomInfoInDto
 * @description TODO
 * @date 2023/6/12 9:41
 */
@Data
public class SimpleSeriesOutDto {
    private Long seriesId;
    private String institutionName;
    private String patientNumber;
    private String patientName;
    private String patientSex;
    private String patientAge;
    private String modality;
    private Double sliceThickness;
    private LocalDateTime createdAt;
}
