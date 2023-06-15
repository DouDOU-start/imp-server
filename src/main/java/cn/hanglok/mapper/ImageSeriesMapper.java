package cn.hanglok.mapper;

import cn.hanglok.dto.SimpleSeriesOutDto;
import cn.hanglok.entity.ImageSeries;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 系列表 Mapper 接口
 * </p>
 *
 * @author Allen
 * @since 2023-06-07
 */
public interface ImageSeriesMapper extends BaseMapper<ImageSeries> {

    List<SimpleSeriesOutDto> getSimpleSeriesList(Long institutionId, String modality, String patientSex, String sliceRange, int pageSize, int offset);

    List<String> getModality();

}
