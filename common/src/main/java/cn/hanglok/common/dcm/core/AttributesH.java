package cn.hanglok.common.dcm.core;

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
public class AttributesH {
    public static Attributes parseAttributes(File file) {
        try {
            DicomInputStream dis = new DicomInputStream(file);
            dis.setIncludeBulkData(DicomInputStream.IncludeBulkData.URI);
            Attributes attr = dis.readDataset();
            dis.close();
            return attr;
        } catch (IOException e) {
            throw new RuntimeException(String.format("%s may not be dicom file!", file.getAbsolutePath()));
        }
    }
}
