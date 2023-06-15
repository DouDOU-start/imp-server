package cn.hanglok.mapper;

import cn.hanglok.dto.SimpleSeriesOutDto;

import java.util.List;

/**
 * @author Allen
 */
public interface DicomInfoMapper {
    List<SimpleSeriesOutDto> getSimpleDicomInfo(Long institutionId, String modality, String patientSex, String sliceRange, int pageSize, int offset);

    List<String> getModality();
}
