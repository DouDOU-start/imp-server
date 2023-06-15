package cn.hanglok.service.impl;

import cn.hanglok.config.FileConfig;
import cn.hanglok.dto.DicomInfoDto;
import cn.hanglok.service.FileService;
import cn.hanglok.util.DicomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;

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
    DicomServiceImpl dicomService;

    @Override
    public DicomInfoDto uploadDicom(File file) {

        File dir;

        DicomInfoDto dicomInfo = DicomUtils.loadDicom(file);
        if (null != dicomInfo) {
            String uploadFileDir = FileConfig.location +
                    File.separator + dicomInfo.getInstitution().getInstitutionName() +
                    File.separator + dicomInfo.getInstitutionPatient().getPatientId() +
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
}
