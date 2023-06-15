package cn.hanglok.service.impl;

import cn.hanglok.entity.Institution;
import cn.hanglok.mapper.InstitutionMapper;
import cn.hanglok.service.IInstitutionService;
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
    public List<Institution> getInstitutionList() {
        return institutionMapper.selectList(null);
    }

}
