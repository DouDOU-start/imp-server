package cn.hanglok.pacs.mapper;

import cn.hanglok.pacs.entity.BodyPart;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 身体部位表 Mapper 接口
 * </p>
 *
 * @author Allen
 * @since 2023-06-07
 */
public interface BodyPartMapper extends BaseMapper<BodyPart> {
    int updateBodyName(BodyPart bodyPart);
}
