package cn.hanglok.service.impl;

import cn.hanglok.dto.DicomInfoDto;
import cn.hanglok.dto.InstitutionPatientDto;
import cn.hanglok.entity.InstitutionPatient;
import cn.hanglok.mapper.InstitutionPatientMapper;
import cn.hanglok.service.IInstitutionPatientService;
import cn.hanglok.util.ConvertUtils;
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
