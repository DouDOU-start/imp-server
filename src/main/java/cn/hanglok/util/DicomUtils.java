package cn.hanglok.util;

import cn.hanglok.dto.*;
import com.alibaba.fastjson.JSON;
import io.micrometer.common.util.StringUtils;
import org.dcm4che3.data.Attributes;
import org.dcm4che3.data.Tag;
import org.dcm4che3.data.VR;
import org.dcm4che3.io.DicomInputStream;
import org.dcm4che3.io.DicomOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * @author Allen
 * @version 1.0
 * @className DicomUtils
 * @description dicom文件工具类
 * @date 2023/6/5 16:25
 */
public class DicomUtils {

    private static final Logger logger = LoggerFactory.getLogger(DicomUtils.class);

    /**
     * 加载dicom文件
     * @param file 文件
     * @return DicomInfo Dicom文件信息详情
     */
    public static DicomInfoDto loadDicom(File file) {

        // 解析Dicom文件
        Attributes attr = null;
        try {
            DicomInputStream dis = new DicomInputStream(file);
            dis.setIncludeBulkData(DicomInputStream.IncludeBulkData.URI);
            attr = dis.readDataset(-1, -1);
            dis.close();
        } catch (IOException e) {
            logger.error(file.getAbsolutePath() + "不是DCM文件！");
        }

        // 设置编码，防止乱码
        String characterSet = attr.getString(Tag.SpecificCharacterSet);
        if (StringUtils.isBlank(characterSet) || !"GB18030".equals(characterSet)) {
            attr.setString(Tag.SpecificCharacterSet, VR.PN, "GB18030");
            attr.setString(Tag.SpecificCharacterSet, VR.CS, "GB18030");
        }

        if (null == attr.getStrings(Tag.PixelSpacing)) {
             return null;
        }

        Attributes finalAttr = attr;
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        ZoneId zoneId = ZoneId.systemDefault(); // 获取当前系统默认时区

        DicomInfoDto dicomInfo = new DicomInfoDto() {{

            // 设置Attributes属性
            setAttr(finalAttr);

            // 设置机构属性
            InstitutionDto institutionDto = new InstitutionDto(){{
                setInstitutionName(finalAttr.getString(Tag.InstitutionName));
                setInstitutionAddress(finalAttr.getString(Tag.InstitutionAddress, ""));
            }};
            setInstitution(institutionDto);

            // 设置患者属性
            setInstitutionPatient(new InstitutionPatientDto() {{
                setPatientId(finalAttr.getString(Tag.PatientID));
                setPatientName(finalAttr.getString(Tag.PatientName));
                setPatientSex(finalAttr.getString(Tag.PatientSex));
            }});

            // 设置研究属性
            setImageStudies(new ImageStudiesDto() {{
                setStudyId(finalAttr.getString(Tag.StudyID));
                setStudyUid(finalAttr.getString(Tag.StudyInstanceUID));
                setStudyDescription(finalAttr.getString(Tag.StudyDescription, ""));
                Date date = finalAttr.getDate(Tag.StudyDateAndTime);
                if (null != date) {
                    setStudyAt(LocalDateTime.ofInstant(date.toInstant(), zoneId));
                }
//                try {
//                    String studyDate = finalAttr.getString(Tag.StudyDate) + finalAttr.getString(Tag.StudyTime);
//                    Date date;
//                    if (! studyDate.contains("null")) {
//                        date = format.parse(studyDate);
//                        setStudyAt(LocalDateTime.ofInstant(date.toInstant(), zoneId));
//                    }
//                } catch (ParseException e) {
//                    logger.error("研究创建时间解析失败！");
//                    throw new RuntimeException(e);
//                }

                // 设置系列属性
                setImageSeries(new ImageSeriesDto() {{
                    setSeriesNumber(finalAttr.getString(Tag.SeriesNumber));
                    setSeriesUid(finalAttr.getString(Tag.SeriesInstanceUID, ""));
                    setSeriesDescription(finalAttr.getString(Tag.SeriesDescription, ""));
                    setModality(finalAttr.getString(Tag.Modality));
                    setPixelSpacing(String.join(" * ", finalAttr.getStrings(Tag.PixelSpacing)));
                    setSliceThickness(Double.parseDouble(finalAttr.getString(Tag.SliceThickness)));
                    setRow(finalAttr.getInt(Tag.Rows, -1));
                    setColumns(finalAttr.getInt(Tag.Columns, -1));
                    Date date = finalAttr.getDate(Tag.SeriesDateAndTime);
                    if (null != date) {
                        setSeriesAt(LocalDateTime.ofInstant(date.toInstant(), zoneId));
                    }
//                    try {
//                        String seriesDate = finalAttr.getString(Tag.SeriesDate) + finalAttr.getString(Tag.SeriesTime);
//                        Date date;
//                        if (! seriesDate.contains("null")) {
//                            date = format.parse(seriesDate);
//                            setSeriesAt(LocalDateTime.ofInstant(date.toInstant(), zoneId));
//                        }
//                    } catch (ParseException e) {
//                        logger.error("系列创建时间解析失败！");
//                        throw new RuntimeException(e);
//                    }
                    setPatientAge(finalAttr.getString(Tag.PatientAge));

                    // 设置实例属性
                    setImageInstance(new ImageInstanceDto() {{
                        setInstanceNumber(finalAttr.getInt(Tag.InstanceNumber, -1));
                        setInstanceUid(finalAttr.getString(Tag.InstanceCreatorUID, ""));
                        setSliceLocation(finalAttr.getString(Tag.SliceLocation));
                        Date date = finalAttr.getDate(Tag.InstanceCreationDateAndTime);
                        if (null != date) {
                            setInstanceAt(LocalDateTime.ofInstant(date.toInstant(), zoneId));
                        }
                    }});
                }});
            }});
        }};

//        logger.info(file.getName() + " DICOM文件解析结果：" + JSON.toJSONString(dicomInfo));

        return dicomInfo;
    }

    /***
     * DICOM 文件中文转码转存
     * @param sourceFile 源文件
     * @param targetFile 目标文件
     */
    public static void chnTranscoding(File sourceFile, File targetFile) {
        DicomInputStream in = null;
        Attributes attr;
        DicomOutputStream dout = null;
        try {
            in = new DicomInputStream(sourceFile);
            attr = in.readDataset();

            String characterSet = attr.getString(Tag.SpecificCharacterSet);
            if (StringUtils.isBlank(characterSet) || !"GB18030".equals(characterSet)) { // 设置编码，防止乱码
                attr.setString(Tag.SpecificCharacterSet, VR.PN, "GB18030");
                attr.setString(Tag.SpecificCharacterSet, VR.CS, "GB18030");

                dout = new DicomOutputStream(targetFile);

                //Attribute写入，完成更改
                dout.writeDataset(in.getFileMetaInformation(), attr);
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
     *  校验dicom文件
     * @param file 文件
     * @return boolean
     */
    public static boolean verify(File file)  {
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] fileHeader = new byte[256];
            fis.read(fileHeader);
            // dicom文件忽略前128字节后，前4字节为DICM
            return "DICM".equals(new String(fileHeader, 128, 4));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

}
