package cn.hanglok.mapper;

import cn.hanglok.entity.ImageInstances;
import cn.hanglok.entity.InstanceLocation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 实例表 Mapper 接口
 * </p>
 *
 * @author Allen
 * @since 2023-06-07
 */
public interface ImageInstancesMapper extends BaseMapper<ImageInstances> {
    InstanceLocation getInstanceLocation(String instanceId);
}
