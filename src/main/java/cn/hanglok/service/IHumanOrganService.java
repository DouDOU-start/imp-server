package cn.hanglok.service;

import cn.hanglok.entity.HumanOrgan;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 器官表 服务类
 * </p>
 *
 * @author Allen
 * @since 2023-06-07
 */
public interface IHumanOrganService extends IService<HumanOrgan> {

    int addHumanOrgan(HumanOrgan humanOrgan);

    int modifyHumanOrgan(HumanOrgan humanOrgan);

    int delHumanOrgan(Long id);
}
