package cn.hanglok.service;

import cn.hanglok.dto.DicomInfoDto;
import cn.hanglok.entity.InstitutionPatient;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 患者表 服务类
 * </p>
 *
 * @author Allen
 * @since 2023-06-07
 */
public interface IInstitutionPatientService extends IService<InstitutionPatient> {

    void addPatient(DicomInfoDto dicomInfo);
}
