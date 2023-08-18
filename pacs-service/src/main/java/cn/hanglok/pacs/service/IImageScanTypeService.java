package cn.hanglok.pacs.service;

import cn.hanglok.pacs.entity.ImageScanType;
import com.baomidou.mybatisplus.core.metadata.IPage;
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

    IPage<ImageScanType> selectPage(String keyword, Integer currentPage, Integer pageSize);
}
