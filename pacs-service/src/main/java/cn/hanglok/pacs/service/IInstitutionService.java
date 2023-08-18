package cn.hanglok.pacs.service;

import cn.hanglok.pacs.dto.DicomInfoDto;
import cn.hanglok.pacs.entity.Institution;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 机构表 服务类
 * </p>
 *
 * @author Allen
 * @since 2023-06-07
 */
public interface IInstitutionService extends IService<Institution> {

    void addInstitution(DicomInfoDto dicomInfo);

    List<Institution> getInstitutionList();

    IPage<Institution> selectPage(String keyword, Integer currentPage, Integer pageSize);

}
