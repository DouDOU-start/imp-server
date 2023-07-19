package cn.hanglok.entity;

import cn.hanglok.config.FileConfig;
import lombok.Data;

import java.io.File;

/**
 * @author Allen
 * @version 1.0
 * @className InstanceLocation
 * @description TODO
 * @date 2023/7/18 17:18
 */
@Data
public class InstanceLocation {
    private String institutionName;
    private String patientNumber;
    private String studyUid;
    private String seriesUid;
    private String instanceNumber;

    @Override
    public String toString() {
        return FileConfig.dicomLocation + File.separator +
                institutionName + File.separator +
                patientNumber + File.separator +
                studyUid + File.separator +
                seriesUid + File.separator +
                instanceNumber + ".dcm";
    }
}
