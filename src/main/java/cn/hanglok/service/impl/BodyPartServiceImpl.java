package cn.hanglok.service.impl;

import cn.hanglok.entity.BodyPart;
import cn.hanglok.mapper.BodyPartMapper;
import cn.hanglok.service.IBodyPartService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 * 身体部位表 服务实现类
 * </p>
 *
 * @author Allen
 * @since 2023-06-07
 */
@Service
public class BodyPartServiceImpl extends ServiceImpl<BodyPartMapper, BodyPart> implements IBodyPartService {

    @Autowired
    BodyPartMapper bodyPartMapper;

    @Override
    public int addBodyPart(BodyPart bodyPartDto) {

        if (null != bodyPartDto.getId()) {
            return -1;
        }

        BodyPart bodyPart = bodyPartMapper.selectOne(new QueryWrapper<>() {{
            eq("body_name", bodyPartDto.getBodyName());
        }});

        return null != bodyPart ? -1 : bodyPartMapper.insert(bodyPartDto);

    }

    @Override
    public int modifyBodyPart(BodyPart bodyPartDto) {
        bodyPartDto.setUpdatedAt(LocalDateTime.now());
        bodyPartDto.setUpdater(99L);
        return bodyPartMapper.updateBodyName(bodyPartDto);
    }

    @Override
    public int delBodyPart(long id) {
        return bodyPartMapper.deleteById(id);
    }
}
