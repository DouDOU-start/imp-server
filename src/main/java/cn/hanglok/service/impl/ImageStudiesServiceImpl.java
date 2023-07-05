package cn.hanglok.service.impl;

import cn.hanglok.dto.DicomInfoDto;
import cn.hanglok.dto.ImageStudiesDto;
import cn.hanglok.entity.ImageStudies;
import cn.hanglok.mapper.ImageStudiesMapper;
import cn.hanglok.service.IImageStudiesService;
import cn.hanglok.util.ConvertUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 研究表 服务实现类
 * </p>
 *
 * @author Allen
 * @since 2023-06-07
 */
@Service
public class ImageStudiesServiceImpl extends ServiceImpl<ImageStudiesMapper, ImageStudies> implements IImageStudiesService {

    @Autowired
    ImageStudiesMapper imageStudiesMapper;

    @Override
    public synchronized void addStudies(DicomInfoDto dicomInfo) {
        ImageStudies studies = imageStudiesMapper.selectOne(new QueryWrapper<>() {{
            eq("study_uid", dicomInfo.getImageStudies().getStudyUid());
        }});

        if (studies == null) {
            dicomInfo.getImageStudies().setPatientId(dicomInfo.getInstitutionPatient().getId());
            imageStudiesMapper.insert(dicomInfo.getImageStudies());
            studies = dicomInfo.getImageStudies();
        }

        ImageStudiesDto imageStudiesDto = ConvertUtils.entityToDto(studies, ImageStudies.class, ImageStudiesDto.class);
        imageStudiesDto.setImageSeries(dicomInfo.getImageStudies().getImageSeries());
        dicomInfo.setImageStudies(imageStudiesDto);

    }
}
