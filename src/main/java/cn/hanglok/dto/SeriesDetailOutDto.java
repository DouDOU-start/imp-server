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
//    private List<Integer> bodyPart;
//    private List<Integer> scanType;
    private Integer bodyPart;
    private Integer scanType;

    public SeriesDetailOutDto(String institutionName, String patientNumber, String patientName, String patientSex, String bodyPart, String scanType) {
        this.institutionName = institutionName;
        this.patientNumber = patientNumber;
        this.patientName = patientName;
        this.patientSex = patientSex;


        if (null != bodyPart) {
            this.bodyPart = Integer.valueOf(bodyPart);
//            this.bodyPart = Arrays.stream(bodyPart.split(","))
//                    .map(String::trim)
//                    .map(Integer::parseInt)
//                    .collect(Collectors.toList());
        } else {
//            this.bodyPart = List.of();
        }

        if (null != scanType) {
//            this.scanType = Arrays.stream(scanType.split(","))
//                    .map(String::trim)
//                    .map(Integer::parseInt)
//                    .collect(Collectors.toList());
            this.scanType = Integer.valueOf(scanType);
        } else {
//            this.scanType = List.of();
        }

    }
}
