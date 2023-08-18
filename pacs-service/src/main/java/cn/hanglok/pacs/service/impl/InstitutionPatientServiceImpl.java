package cn.hanglok.pacs.service.impl;

import cn.hanglok.pacs.dto.DicomInfoDto;
import cn.hanglok.pacs.dto.InstitutionPatientDto;
import cn.hanglok.pacs.entity.InstitutionPatient;
import cn.hanglok.pacs.mapper.InstitutionPatientMapper;
import cn.hanglok.pacs.service.IInstitutionPatientService;
import cn.hanglok.pacs.util.ConvertUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 患者表 服务实现类
 * </p>
 *
 * @author Allen
 * @since 2023-06-07
 */
@Service
public class InstitutionPatientServiceImpl extends ServiceImpl<InstitutionPatientMapper, InstitutionPatient> implements IInstitutionPatientService {

    @Autowired
    InstitutionPatientMapper institutionPatientMapper;

    @Override
    public synchronized void addPatient(DicomInfoDto dicomInfo) {
        InstitutionPatient patient = institutionPatientMapper.selectOne(new QueryWrapper<>() {{
            eq("patient_number", dicomInfo.getInstitutionPatient().getPatientNumber());
            eq("institution_id", dicomInfo.getInstitution().getId());
        }});

        if (patient == null) {
            dicomInfo.getInstitutionPatient().setInstitutionId(dicomInfo.getInstitution().getId());
            institutionPatientMapper.insert(dicomInfo.getInstitutionPatient());
            patient = dicomInfo.getInstitutionPatient();
        }

        dicomInfo.setInstitutionPatient(ConvertUtils.entityToDto(patient, InstitutionPatient.class, InstitutionPatientDto.class));
    }
}
