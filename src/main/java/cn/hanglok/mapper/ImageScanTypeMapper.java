package cn.hanglok.mapper;

import cn.hanglok.entity.ImageScanType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 扫描类型表 Mapper 接口
 * </p>
 *
 * @author Allen
 * @since 2023-06-07
 */
public interface ImageScanTypeMapper extends BaseMapper<ImageScanType> {
    int updateScanTypeName(ImageScanType imageScanType);
}
