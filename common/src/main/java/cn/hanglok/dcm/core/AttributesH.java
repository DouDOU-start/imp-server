package cn.hanglok.dcm.core;

import lombok.extern.slf4j.Slf4j;
import org.dcm4che3.data.Attributes;
import org.dcm4che3.io.DicomInputStream;

import java.io.File;
import java.io.IOException;

/**
 * @author Allen
 * @version 1.0
 * @className Attributes
 * @description TODO
 * @date 2023/9/5
 */
@Slf4j
public class AttributesH {
    public static Attributes parseAttributes(File file) {
        try {
            DicomInputStream dis = new DicomInputStream(file);
            dis.setIncludeBulkData(DicomInputStream.IncludeBulkData.URI);
            org.dcm4che3.data.Attributes attr = dis.readDataset();
            dis.close();
            return attr;
        } catch (IOException e) {
            log.error(file.getAbsolutePath() + "不是DCM文件！");
        }
        return null;
    }
}
