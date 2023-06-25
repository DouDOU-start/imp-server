package cn.hanglok.service;

import cn.hanglok.dto.SimpleSeriesOutDto;
import cn.hanglok.entity.ImageSeries;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 系列表 服务类
 * </p>
 *
 * @author Allen
 * @since 2023-06-07
 */
public interface IImageSeriesService extends IService<ImageSeries> {
    IPage<SimpleSeriesOutDto> getSimpleSeriesList(String keyword, Long[] institutionId, String[] modality, Double[] sliceRange, Long[] bodyPartIds,
                                                  String patientSex, Long[] organIds, Long[] scanTypeIds, int currentPage, int pageSize);

    List<String> getModality();
}
