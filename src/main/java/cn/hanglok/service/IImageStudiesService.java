package cn.hanglok.service;

import cn.hanglok.dto.DicomInfoDto;
import cn.hanglok.entity.ImageStudies;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 研究表 服务类
 * </p>
 *
 * @author Allen
 * @since 2023-06-07
 */
public interface IImageStudiesService extends IService<ImageStudies> {
    void addStudies(DicomInfoDto dicomInfo);
}
