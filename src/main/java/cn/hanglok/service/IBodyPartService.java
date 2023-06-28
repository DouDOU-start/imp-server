package cn.hanglok.service;

import cn.hanglok.entity.BodyPart;
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

}
