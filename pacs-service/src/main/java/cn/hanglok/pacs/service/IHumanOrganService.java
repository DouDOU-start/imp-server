package cn.hanglok.pacs.service;

import cn.hanglok.pacs.entity.HumanOrgan;
import com.baomidou.mybatisplus.core.metadata.IPage;
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

    IPage<HumanOrgan> selectPage(String keyword, Integer currentPage, Integer pageSize);
}
