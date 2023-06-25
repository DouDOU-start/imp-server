package cn.hanglok.service.impl;

import cn.hanglok.dto.*;
import cn.hanglok.entity.*;
import cn.hanglok.mapper.*;
import cn.hanglok.service.IImageSeriesService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 系列表 服务实现类
 * </p>
 *
 * @author Allen
 * @since 2023-06-07
 */
@Service
public class ImageSeriesServiceImpl extends ServiceImpl<ImageSeriesMapper, ImageSeries> implements IImageSeriesService {

    @Autowired
    ImageSeriesMapper imageSeriesMapper;

    @Override
    public IPage<SimpleSeriesOutDto> getSimpleSeriesList(String keyword, Long[] institutionIds, String[] modality, Double[] sliceRange, Long[] bodyPartIds,
                                                        String patientSex, Long[] organIds, Long[] scanTypeIds, int currentPage, int pageSize) {

        return imageSeriesMapper.getSimpleSeriesList(
                keyword,
                institutionIds,
                modality,
                sliceRange,
                bodyPartIds,
                patientSex,
                organIds,
                scanTypeIds,
                new Page<>(currentPage, pageSize) {{
                    setOptimizeCountSql(false);
                }}
        );
    }

    @Override
    public List<String> getModality() {
        return imageSeriesMapper.getModality();
    }
}
