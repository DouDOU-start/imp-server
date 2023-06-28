package cn.hanglok.service.impl;

import cn.hanglok.entity.HumanOrgan;
import cn.hanglok.mapper.HumanOrganMapper;
import cn.hanglok.service.IHumanOrganService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 * 器官表 服务实现类
 * </p>
 *
 * @author Allen
 * @since 2023-06-07
 */
@Service
public class HumanOrganServiceImpl extends ServiceImpl<HumanOrganMapper, HumanOrgan> implements IHumanOrganService {

    @Autowired
    HumanOrganMapper humanOrganMapper;

    @Override
    public int addHumanOrgan(HumanOrgan humanOrganDto) {
        if (null != humanOrganDto.getId()) {
            return -1;
        }

        HumanOrgan humanOrgan = humanOrganMapper.selectOne(new QueryWrapper<>() {{
            eq("organ_name", humanOrganDto.getOrganName());
        }});

        return null != humanOrgan ? -1 : humanOrganMapper.insert(humanOrganDto);
    }

    @Override
    public int modifyHumanOrgan(HumanOrgan humanOrgan) {
        humanOrgan.setUpdatedAt(LocalDateTime.now());
        humanOrgan.setUpdater(99L);
        return humanOrganMapper.updateOrganName(humanOrgan);
    }

    @Override
    public int delHumanOrgan(Long id) {
        return humanOrganMapper.deleteById(id);
    }
}
