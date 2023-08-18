package cn.hanglok.pacs.entity;

import lombok.Data;

import java.io.File;

/**
 * @author Allen
 * @version 1.0
 * @className SeriesTree
 * @description 系列路径树
 * @date 2023/7/6 16:39
 */
@Data
public class SeriesTree {
    private String institutionName;
    private String patientNumber;
    private String studyUid;
    private String seriesUid;

    @Override
    public String toString() {
        return File.separator + institutionName +
                File.separator + patientNumber +
                File.separator + studyUid +
                File.separator + seriesUid;
    }
}
