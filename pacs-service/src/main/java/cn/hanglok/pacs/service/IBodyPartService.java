package cn.hanglok.pacs.service;

import cn.hanglok.pacs.entity.BodyPart;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 身体部位表 服务类
 * </p>
 *
 * @author Allen
 * @since 2023-06-07
 */
public interface IBodyPartService extends IService<BodyPart> {

    int addBodyPart(BodyPart bodyPartDto);

    int modifyBodyPart(BodyPart bodyPartDto);

    int delBodyPart(long id);

    IPage<BodyPart> selectPage(String keyword, Integer currentPage, Integer pageSize);

}
