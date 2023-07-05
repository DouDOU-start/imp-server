package cn.hanglok.service;

import cn.hanglok.dto.DicomInfoDto;
import cn.hanglok.entity.ImageInstances;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 实例表 服务类
 * </p>
 *
 * @author Allen
 * @since 2023-06-07
 */
public interface IImageInstancesService extends IService<ImageInstances> {
    void addInstances(DicomInfoDto dicomInfo);
}
