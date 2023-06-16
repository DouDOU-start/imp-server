package cn.hanglok.service.impl;

import cn.hanglok.dto.*;
import cn.hanglok.entity.*;
import cn.hanglok.mapper.*;
import cn.hanglok.service.DicomService;
import cn.hanglok.util.ConvertUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
    InstitutionMapper institutionMapper;

    @Autowired
    InstitutionPatientMapper institutionPatientMapper;

    @Autowired
    ImageStudiesMapper imageStudiesMapper;

    @Autowired
    ImageSeriesMapper imageSeriesMapper;

    @Autowired
    ImageInstancesMapper imageInstancesMapper;

    @Override
    public DicomInfoDto save(DicomInfoDto dicomInfo) {

        // 判断机构是否存在
        Institution institution = institutionMapper.selectOne(new QueryWrapper<>() {{
            eq("institution_name", dicomInfo.getInstitution().getInstitutionName());
        }});

        if (institution == null) {
            institutionMapper.insert(dicomInfo.getInstitution());
            institution = dicomInfo.getInstitution();
        }

        Institution finalInstitution = institution;

        // 判断患者是否存在
        InstitutionPatient patient = institutionPatientMapper.selectOne(new QueryWrapper<>() {{
            eq("patient_id", dicomInfo.getInstitutionPatient().getPatientId());
            eq("institution_id", finalInstitution.getId());
        }});

        if (patient == null) {
            dicomInfo.getInstitutionPatient().setInstitutionId(institution.getId());
            institutionPatientMapper.insert(dicomInfo.getInstitutionPatient());
            patient = dicomInfo.getInstitutionPatient();
        }

        // 判断研究是否存在
        ImageStudies studies = imageStudiesMapper.selectOne(new QueryWrapper<>() {{
            eq("study_uid", dicomInfo.getImageStudies().getStudyUid());
        }});

        if (studies == null) {
            dicomInfo.getImageStudies().setPatientId(patient.getId());
            imageStudiesMapper.insert(dicomInfo.getImageStudies());
            studies = dicomInfo.getImageStudies();
        }

        // 判断系列是否存在
//        ImageStudies finalStudies = studies;
        ImageSeries series = imageSeriesMapper.selectOne(new QueryWrapper<>() {{
            eq("series_uid", dicomInfo.getImageStudies().getImageSeries().getSeriesUid());
        }});

        if (series == null) {
            dicomInfo.getImageStudies().getImageSeries().setStudyId(studies.getId());
            imageSeriesMapper.insert(dicomInfo.getImageStudies().getImageSeries());
            series = dicomInfo.getImageStudies().getImageSeries();
        }

        // 判断实例是否存在
        ImageSeries finalSeries = series;
        ImageInstances instances = imageInstancesMapper.selectOne(new QueryWrapper<>() {{
            eq("instance_number", dicomInfo.getImageStudies().getImageSeries().getImageInstance().getInstanceNumber());
            eq("series_id", finalSeries.getId());
        }});

        if (instances == null) {
            dicomInfo.getImageStudies().getImageSeries().getImageInstance().setSeriesId(series.getId());
            imageInstancesMapper.insert(dicomInfo.getImageStudies().getImageSeries().getImageInstance());
            instances = dicomInfo.getImageStudies().getImageSeries().getImageInstance();
        }



        DicomInfoDto finalDicomInfo = new DicomInfoDto();

        // 设置机构属性
        finalDicomInfo.setInstitution(ConvertUtils.entityToDto(institution, Institution.class, InstitutionDto.class));

        // 设置患者属性
        finalDicomInfo.setInstitutionPatient(ConvertUtils.entityToDto(patient, InstitutionPatient.class, InstitutionPatientDto.class));

        ImageStudiesDto imageStudies = ConvertUtils.entityToDto(studies, ImageStudies.class, ImageStudiesDto.class);
        ImageSeriesDto imageSeries = ConvertUtils.entityToDto(series, ImageSeries.class, ImageSeriesDto.class);

        // 设置实例属性
        imageSeries.setImageInstance(ConvertUtils.entityToDto(instances, ImageInstances.class, ImageInstanceDto.class));
        // 设置系列属性
        imageStudies.setImageSeries(imageSeries);
        // 设置研究属性
        finalDicomInfo.setImageStudies(imageStudies);

        return finalDicomInfo;

    }

}
