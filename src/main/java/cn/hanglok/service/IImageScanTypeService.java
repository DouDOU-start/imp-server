package cn.hanglok.service;

import cn.hanglok.entity.ImageScanType;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 扫描类型表 服务类
 * </p>
 *
 * @author Allen
 * @since 2023-06-07
 */
public interface IImageScanTypeService extends IService<ImageScanType> {

    int addScanType(ImageScanType imageScanTypeDto);

    int modifyScanType(ImageScanType imageScanTypeDto);

    int delScanType(Long id);
}
