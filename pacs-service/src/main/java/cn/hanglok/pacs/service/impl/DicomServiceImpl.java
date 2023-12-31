package cn.hanglok.pacs.service.impl;

import cn.hanglok.pacs.dto.DicomInfoDto;
import cn.hanglok.pacs.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Allen
 * @version 1.0
 * @className DicomServiceImpl
 * @description TODO
 * @date 2023/6/7 16:08
 */
@Service
public class DicomServiceImpl implements DicomService {

    @Autowired
    IInstitutionService institutionService;

    @Autowired
    IInstitutionPatientService institutionPatientService;

    @Autowired
    IImageStudiesService imageStudiesService;

    @Autowired
    IImageSeriesService imageSeriesService;

    @Autowired
    IImageInstancesService imageInstancesService;

    @Autowired
    IDictionaryService dictionaryService;

    @Override
    public DicomInfoDto save(DicomInfoDto dicomInfo) {

        // 添加机构信息
        institutionService.addInstitution(dicomInfo);

        // 添加医院患者信息
        institutionPatientService.addPatient(dicomInfo);

        // 添加研究信息
        imageStudiesService.addStudies(dicomInfo);

        // 添加系列信息
        imageSeriesService.addImageSeries(dicomInfo);

        // 添加实例信息
        imageInstancesService.addInstances(dicomInfo);

        // 更新系列实例数
        imageSeriesService.updateInstanceNum(dicomInfo.getImageStudies().getImageSeries().getId());

        // 更新字典
        dictionaryService.updateModality();

        return dicomInfo;

    }

}
