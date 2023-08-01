package cn.hanglok.service.impl;

import cn.hanglok.dto.DicomInfoDto;
import cn.hanglok.dto.ImageSeriesDto;
import cn.hanglok.dto.SeriesDetailOutDto;
import cn.hanglok.dto.SimpleSeriesOutDto;
import cn.hanglok.entity.ImageSeries;
import cn.hanglok.entity.SeriesTree;
import cn.hanglok.mapper.*;
import cn.hanglok.service.IImageSeriesService;
import cn.hanglok.util.ConvertUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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

    @Autowired
    ImageStudiesMapper imageStudiesMapper;

    @Autowired
    InstitutionPatientMapper institutionPatientMapper;

    @Autowired
    InstitutionMapper institutionMapper;

    @Autowired
    ImageInstancesMapper imageInstancesMapper;

    @Autowired
    SeriesBodyPartMapper seriesBodyPartMapper;


    @Override
    public synchronized void addImageSeries(DicomInfoDto dicomInfo) {
        ImageSeries series = imageSeriesMapper.selectOne(new QueryWrapper<>() {{
            eq("series_uid", dicomInfo.getImageStudies().getImageSeries().getSeriesUid());
        }});

        if (series == null) {
            dicomInfo.getImageStudies().getImageSeries().setStudyId(dicomInfo.getImageStudies().getId());
            imageSeriesMapper.insert(dicomInfo.getImageStudies().getImageSeries());
            series = dicomInfo.getImageStudies().getImageSeries();
        }

        ImageSeriesDto imageSeriesDto = ConvertUtils.entityToDto(series, ImageSeries.class, ImageSeriesDto.class);
        imageSeriesDto.setImageInstance(dicomInfo.getImageStudies().getImageSeries().getImageInstance());
        dicomInfo.getImageStudies().setImageSeries(imageSeriesDto);
    }

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

    @Override
    public SeriesTree getTree(Long seriesId) {
        return imageSeriesMapper.getTree(seriesId);
    }

    @Override
    public SeriesDetailOutDto getSeriesDetail(String seriesId) {
        return imageSeriesMapper.getSeriesDetail(seriesId);
    }

    @Override
    public void updateInstanceNum(Long seriesId) {
        Long instanceNum = imageInstancesMapper.selectCount(new QueryWrapper<>() {{
            eq("series_id", seriesId);
        }});
        imageSeriesMapper.updateInstanceNum(seriesId, instanceNum);
    }
}
