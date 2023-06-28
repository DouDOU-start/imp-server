package cn.hanglok.service.impl;

import cn.hanglok.entity.ImageScanType;
import cn.hanglok.mapper.ImageScanTypeMapper;
import cn.hanglok.service.IImageScanTypeService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * <p>
 * 扫描类型表 服务实现类
 * </p>
 *
 * @author Allen
 * @since 2023-06-07
 */
@Service
public class ImageScanTypeServiceImpl extends ServiceImpl<ImageScanTypeMapper, ImageScanType> implements IImageScanTypeService {

    @Autowired
    ImageScanTypeMapper imageScanTypeMapper;

    @Override
    public int addScanType(ImageScanType imageScanTypeDto) {
        if (null != imageScanTypeDto.getId()) {
            return -1;
        }

        ImageScanType imageScanType = imageScanTypeMapper.selectOne(new QueryWrapper<>() {{
            eq("scan_type_name", imageScanTypeDto.getScanTypeName());
        }});

        return null != imageScanType ? -1 : imageScanTypeMapper.insert(imageScanTypeDto);
    }

    @Override
    public int modifyScanType(ImageScanType imageScanTypeDto) {
        ImageScanType imageScanType = imageScanTypeMapper.selectOne(new QueryWrapper<>() {{
            eq("scan_type_name", imageScanTypeDto.getScanTypeName());
        }});

        if (null != imageScanType && ! Objects.equals(imageScanType.getId(), imageScanTypeDto.getId())) {
            return -1;
        }

        imageScanTypeDto.setUpdatedAt(LocalDateTime.now());
        imageScanTypeDto.setUpdater(99L);
        return imageScanTypeMapper.updateScanTypeName(imageScanTypeDto);
    }

    @Override
    public int delScanType(Long id) {
        return imageScanTypeMapper.deleteById(id);
    }
}
