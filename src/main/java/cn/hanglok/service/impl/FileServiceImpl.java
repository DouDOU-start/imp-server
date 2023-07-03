package cn.hanglok.service.impl;

import cn.hanglok.config.FileConfig;
import cn.hanglok.dto.DicomInfoDto;
import cn.hanglok.entity.ImageLabel;
import cn.hanglok.entity.ImageSeries;
import cn.hanglok.mapper.ImageSeriesMapper;
import cn.hanglok.service.DicomService;
import cn.hanglok.service.FileService;
import cn.hanglok.service.IImageLabelService;
import cn.hanglok.util.DicomUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

/**
 * @author Allen
 * @version 1.0
 * @className FileServiceImpl
 * @description TODO
 * @date 2023/6/6 13:44
 */
@Service
public class FileServiceImpl implements FileService {

    private final Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    @Autowired
    FileConfig fileConfig;

    @Autowired
    DicomService dicomService;

    @Autowired
    IImageLabelService imageLabelService;

    @Autowired
    ImageSeriesMapper imageSeriesMapper;

    @Override
    public DicomInfoDto uploadDicom(File file) {

        File dir;

        DicomInfoDto dicomInfo = DicomUtils.loadDicom(file);
        if (null != dicomInfo) {
            String uploadFileDir = FileConfig.dicomLocation +
                    File.separator + dicomInfo.getInstitution().getInstitutionName() +
                    File.separator + dicomInfo.getInstitutionPatient().getPatientNumber() +
                    File.separator + dicomInfo.getImageStudies().getStudyUid() +
                    File.separator + dicomInfo.getImageStudies().getImageSeries().getSeriesUid();

            if (!(dir = new File(uploadFileDir)).exists()) {
                dir.mkdirs();
            }

            File uploadFile = new File(uploadFileDir + File.separator +
                    dicomInfo.getImageStudies().getImageSeries().getImageInstance().getInstanceNumber() + ".dcm");

            DicomUtils.chnTranscoding(file, uploadFile);

            return dicomService.save(dicomInfo);
        }

        return null;

    }

    @Override
    public boolean uploadLabel(MultipartFile file, String seriesId) {

        // 不允许上传空文件
        if (file.isEmpty()) {
            return false;
        }

        ImageSeries imageSeries = imageSeriesMapper.selectOne(new QueryWrapper<>() {{
            eq("id", seriesId);
        }});

        if (null == imageSeries) {
            return false;
        }

        try {

            String fileName = file.getOriginalFilename();
            if (null != fileName) {

                // 备份覆盖标签文件移动到回收站
                File[] files = new File(FileConfig.labelLocation)
                        .listFiles((dir1, name) -> name.substring(0, name.indexOf(".")).equals(seriesId));
                for (File bakFile : files) {
                    Files.move(
                            bakFile.toPath(),
                            new File(FileConfig.labelBakLocation + File.separator + bakFile.getName()).toPath(),
                            StandardCopyOption.REPLACE_EXISTING
                    );
                }

                // 保存文件到本地
                String fileLocation = FileConfig.labelLocation + File.separator + seriesId + fileName.substring(fileName.indexOf("."));
                file.transferTo(new File(fileLocation));

                imageLabelService.saveOrUpdateLabel(new ImageLabel() {{
                    setFileName(fileName);
                    setFileLocation(fileLocation);
                    setSeriesId(Long.valueOf(seriesId));
                }});

                return true;
            }
            return false;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
