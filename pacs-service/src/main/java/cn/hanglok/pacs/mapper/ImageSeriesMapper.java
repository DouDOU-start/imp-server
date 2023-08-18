package cn.hanglok.pacs.mapper;

import cn.hanglok.pacs.dto.SeriesDetailOutDto;
import cn.hanglok.pacs.dto.SimpleSeriesOutDto;
import cn.hanglok.pacs.entity.ImageSeries;
import cn.hanglok.pacs.entity.SeriesTree;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

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

    IPage<SimpleSeriesOutDto> getSimpleSeriesList(String keyword,
                                                  Long[] institutionIds,
                                                  String[] modality,
                                                  Double[] sliceRange,
                                                  Long[] bodyPartIds,
                                                  String patientSex,
                                                  Long[] organIds,
                                                  Long[] scanTypeIds,
                                                  Page<SimpleSeriesOutDto> page);

    List<String> getModality();

    SeriesTree getTree(Long seriesId);

    void updateInstanceNum(Long seriesId, Long instanceNum);

    SeriesDetailOutDto getSeriesDetail(String seriesId);

}
