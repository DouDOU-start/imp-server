package cn.hanglok.service;

import cn.hanglok.dto.ModifySeriesScanTypeDto;
import cn.hanglok.entity.SeriesScanType;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 系列对应扫描类型表 服务类
 * </p>
 *
 * @author Allen
 * @since 2023-07-17
 */
public interface ISeriesScanTypeService extends IService<SeriesScanType> {
    int modifyScanType(ModifySeriesScanTypeDto modifySeriesScanType);
}
