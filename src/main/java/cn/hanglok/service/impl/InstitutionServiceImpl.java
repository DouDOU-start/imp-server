package cn.hanglok.service.impl;

import cn.hanglok.dto.DicomInfoDto;
import cn.hanglok.dto.InstitutionDto;
import cn.hanglok.entity.Institution;
import cn.hanglok.mapper.InstitutionMapper;
import cn.hanglok.service.IInstitutionService;
import cn.hanglok.util.ConvertUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 机构表 服务实现类
 * </p>
 *
 * @author Allen
 * @since 2023-06-07
 */
@Service
public class InstitutionServiceImpl extends ServiceImpl<InstitutionMapper, Institution> implements IInstitutionService {

    @Autowired
    InstitutionMapper institutionMapper;

    @Override
    public synchronized void addInstitution(DicomInfoDto dicomInfo) {
        Institution institution = institutionMapper.selectOne(new QueryWrapper<>() {{
            eq("institution_name", dicomInfo.getInstitution().getInstitutionName());
        }});

        if (institution == null) {
            institutionMapper.insert(dicomInfo.getInstitution());
            institution = dicomInfo.getInstitution();
        }

        dicomInfo.setInstitution(ConvertUtils.entityToDto(institution, Institution.class, InstitutionDto.class));
    }

    @Override
    public List<Institution> getInstitutionList() {
        return institutionMapper.selectList(null);
    }

}
