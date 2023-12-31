package cn.hanglok.pacs.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * @author Allen
 * @version 1.0
 * @className FileConfig
 * @description TODO
 * @date 2023/6/6 14:18
 */
@Component
@ConfigurationProperties(prefix = "pacs.file")
public class FileConfig {
    public static String dicomLocation;
    public static String labelLocation;
    public static String labelBakLocation;

    public static String tmpLocation;

    public static int buffSize;

    public void setLocation(String location) {
        FileConfig.dicomLocation = location + File.separator + "dicom";
        FileConfig.labelLocation = location + File.separator + "label";
        FileConfig.labelBakLocation = location + File.separator + "labelBak";
        FileConfig.tmpLocation = location + File.separator + "tmp";
    }

    public void setBuffSize(int buffSize) {
        FileConfig.buffSize = buffSize;
    }

}
