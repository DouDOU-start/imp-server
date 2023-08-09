package cn.hanglok.mapper;

import cn.hanglok.dto.ImageLabelOutDto;
import cn.hanglok.entity.ImageLabel;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 标注文件表 Mapper 接口
 * </p>
 *
 * @author Allen
 * @since 2023-06-07
 */
public interface ImageLabelMapper extends BaseMapper<ImageLabel> {
    int updateFile(ImageLabel imageLabel);

    List<ImageLabelOutDto> getSeriesLabel(String seriesId);
}
