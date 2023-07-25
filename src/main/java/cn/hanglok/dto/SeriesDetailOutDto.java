package cn.hanglok.dto;

import cn.hanglok.entity.ImageSeries;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Allen
 * @version 1.0
 * @className SeriesDetailOutDto
 * @description TODO
 * @date 2023/7/19 10:53
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SeriesDetailOutDto extends ImageSeries {
    private String institutionName;
    private String patientNumber;
    private String patientName;
    private String patientSex;
}
