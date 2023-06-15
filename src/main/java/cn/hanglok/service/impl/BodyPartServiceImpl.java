package cn.hanglok.service.impl;

import cn.hanglok.entity.BodyPart;
import cn.hanglok.mapper.BodyPartMapper;
import cn.hanglok.service.IBodyPartService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
