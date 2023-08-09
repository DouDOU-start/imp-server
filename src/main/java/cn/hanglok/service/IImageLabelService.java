package cn.hanglok.service;

import cn.hanglok.dto.ImageLabelOutDto;
import cn.hanglok.entity.ImageLabel;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 标注文件表 服务类
 * </p>
 *
 * @author Allen
 * @since 2023-06-07
 */
public interface IImageLabelService extends IService<ImageLabel> {

    int saveOrUpdateLabel(ImageLabel imageLabel);

    List<ImageLabelOutDto> getSeriesLabel(String seriesId);

}
