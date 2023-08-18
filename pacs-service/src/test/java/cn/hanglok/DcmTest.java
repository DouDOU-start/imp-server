package cn.hanglok;

import cn.hanglok.pacs.dto.DicomInfoDto;
import cn.hanglok.pacs.util.DicomUtils;
import cn.hanglok.pacs.util.FileUtils;
import io.micrometer.common.util.StringUtils;
import org.dcm4che3.data.Attributes;
import org.dcm4che3.data.Tag;
import org.dcm4che3.data.VR;
import org.dcm4che3.io.DicomInputStream;
import org.dcm4che3.io.DicomOutputStream;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.util.Arrays;
import java.util.function.Function;

/**
 * @author Allen
 * @version 1.0
 * @className DcmTests DCM文件测试
 * @description 用于测试DCM文件加载、修改等功能
 * @date 2023/5/23 17:47
 */

@SpringBootTest
public class DcmTest {

    private static final Logger logger = LoggerFactory.getLogger(DcmTest.class);

    /**
     * 校验imageOrientationPatient属性
     */
    @Test
    void checkImageOrientationPatient() {

        File folder = new File("E:\\hanglok\\dcm\\阳江市人民医院");

        Function<File, Void> func = file -> {
            if (isDicom(file) && ! file.getName().equals("DICOMDIR")) {
                DicomInfoDto dicomInfoDto = DicomUtils.loadDicom(file);
                if (null != dicomInfoDto) {
                    logger.info(file.getAbsolutePath() + " " + Arrays.toString(dicomInfoDto.getAttr().getStrings(Tag.ImageOrientationPatient)));
                } else {
                    logger.error("dicom文件解析失败，文件路径：" + file.getAbsolutePath());
                }
            }
            return null;
        };

        FileUtils.scanFolder(folder, func);

    }

    @Test
    public void DcmTest() {
        File folder = new File("E:\\hanglok\\dcm\\阳江市人民医院\\黄达彬");
        Function<File, Void> func = file -> {
            if (isDicom(file) && ! file.getName().equals("DICOMDIR")) {
                chnTranscoding(file);
            }
            return null;
        };
        FileUtils.scanFolder(folder, func);
    }


    /***
     * DICOM文件中文转码
     * @param file 文件
     */
    public static void chnTranscoding(File file) {
        DicomInputStream in = null;
        Attributes attr;
        DicomOutputStream dout = null;
        try {
            in = new DicomInputStream(file);
            attr = in.readDataset();

            String characterSet = attr.getString(Tag.SpecificCharacterSet);
            if (StringUtils.isBlank(characterSet) || !characterSet.equals("GB18030")) { // 设置编码，防止乱码
                attr.setString(Tag.SpecificCharacterSet, VR.PN, "GB18030");
                attr.setString(Tag.SpecificCharacterSet, VR.CS, "GB18030");

                String outputPath = file.getParent() + "-CN";
                File outputFolder = new File(outputPath);
                if (! outputFolder.exists()) {
                    outputFolder.mkdirs();
                }

                dout = new DicomOutputStream(new File(outputPath + File.separator + file.getName()));

                //Attribute写入，完成更改
                dout.writeDataset(in.getFileMetaInformation(), attr);

                logger.info(file.getAbsolutePath() + " chnTranscoding success");
            }
        } catch (IOException e) {
            logger.error(e.toString());
            throw new RuntimeException(e);
        } finally {
            if (dout != null) {
                try {
                    dout.finish();
                    dout.flush();
                    dout.close();
                } catch (IOException e) {
                    logger.error(e.toString());
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    logger.error(e.toString());
                }
            }
        }
    }

    /**
     *  判断文件是否为dicom文件
     * @param file 文件
     * @return 是否为dicom文件
     */
    public static boolean isDicom(File file)  {
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] fileHeader = new byte[256];
            fis.read(fileHeader);
            // dicom文件忽略前128字节后，前4字节为DICM
            return new String(fileHeader, 128, 4).equals("DICM");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

}
