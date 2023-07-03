package cn.hanglok.service.impl;

import cn.hanglok.entity.ImageLabel;
import cn.hanglok.mapper.ImageLabelMapper;
import cn.hanglok.service.IImageLabelService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 * 标注文件表 服务实现类
 * </p>
 *
 * @author Allen
 * @since 2023-06-07
 */
@Service
public class ImageLabelServiceImpl extends ServiceImpl<ImageLabelMapper, ImageLabel> implements IImageLabelService {

    @Autowired
    ImageLabelMapper imageLabelMapper;

    @Override
    public int saveOrUpdateLabel(ImageLabel imageLabel) {

        ImageLabel existImageLabel = imageLabelMapper.selectOne(new QueryWrapper<>() {{
            eq("series_id", imageLabel.getSeriesId());
        }});

        if (null == existImageLabel) {
            imageLabel.setCreatedAt(LocalDateTime.now());
            imageLabel.setUpdatedAt(LocalDateTime.now());
            return imageLabelMapper.insert(imageLabel);
        }

        imageLabel.setId(existImageLabel.getId());
        imageLabel.setUpdater(99L);
        imageLabel.setUpdatedAt(LocalDateTime.now());

        return imageLabelMapper.updateFile(imageLabel);
    }

}
