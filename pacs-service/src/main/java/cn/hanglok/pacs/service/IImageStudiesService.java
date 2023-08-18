package cn.hanglok.pacs.service;

import cn.hanglok.pacs.dto.DicomInfoDto;
import cn.hanglok.pacs.entity.ImageStudies;
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
