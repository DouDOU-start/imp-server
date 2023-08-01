package cn.hanglok.service.impl;

import cn.hanglok.dto.DicomInfoDto;
import cn.hanglok.dto.ImageInstanceDto;
import cn.hanglok.entity.ImageInstances;
import cn.hanglok.entity.InstanceLocation;
import cn.hanglok.mapper.ImageInstancesMapper;
import cn.hanglok.service.IImageInstancesService;
import cn.hanglok.util.ConvertUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 实例表 服务实现类
 * </p>
 *
 * @author Allen
 * @since 2023-06-07
 */
@Service
public class ImageInstancesServiceImpl extends ServiceImpl<ImageInstancesMapper, ImageInstances> implements IImageInstancesService {

    @Autowired
    ImageInstancesMapper imageInstancesMapper;

    @Override
    public synchronized void addInstances(DicomInfoDto dicomInfo) {
        ImageInstances instances = imageInstancesMapper.selectOne(new QueryWrapper<>() {{
            eq("instance_number", dicomInfo.getImageStudies().getImageSeries().getImageInstance().getInstanceNumber());
            eq("series_id", dicomInfo.getImageStudies().getImageSeries().getId());
        }});

        if (instances == null) {
            dicomInfo.getImageStudies().getImageSeries().getImageInstance().setSeriesId(dicomInfo.getImageStudies().getImageSeries().getId());
            imageInstancesMapper.insert(dicomInfo.getImageStudies().getImageSeries().getImageInstance());
            instances = dicomInfo.getImageStudies().getImageSeries().getImageInstance();
        }

        dicomInfo.getImageStudies().getImageSeries().setImageInstance(ConvertUtils.entityToDto(instances, ImageInstances.class, ImageInstanceDto.class));
    }

    @Override
    public List<ImageInstances> getInstances(String seriesId) {
        return imageInstancesMapper.selectList(new QueryWrapper<>() {{
            eq("series_id", seriesId);
            orderByAsc("instance_number");
        }});
    }

    @Override
    public String getLocation(String instanceId) {
        InstanceLocation instanceLocation = imageInstancesMapper.getInstanceLocation(instanceId);
        return instanceLocation.toString();
    }
}
