package cn.hanglok.pacs.mapper;

import cn.hanglok.pacs.entity.ImageInstances;
import cn.hanglok.pacs.entity.InstanceLocation;
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
