package cn.hanglok.service;

import cn.hanglok.dto.ModifySeriesBodyPartDto;
import cn.hanglok.entity.SeriesBodyPart;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 系列对应身体部位表 服务类
 * </p>
 *
 * @author Allen
 * @since 2023-07-13
 */
public interface ISeriesBodyPartService extends IService<SeriesBodyPart> {
    int modifyBodyPart(ModifySeriesBodyPartDto modifySeriesBodyPart);
}
