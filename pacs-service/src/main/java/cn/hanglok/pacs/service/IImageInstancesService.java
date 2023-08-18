package cn.hanglok.pacs.service;

import cn.hanglok.pacs.dto.DicomInfoDto;
import cn.hanglok.pacs.entity.ImageInstances;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

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

    List<ImageInstances> getInstances(String seriesId);

    String getLocation(String instanceId);
}
