package cn.hanglok.pacs.mapper;

import cn.hanglok.pacs.entity.HumanOrgan;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 器官表 Mapper 接口
 * </p>
 *
 * @author Allen
 * @since 2023-06-07
 */
public interface HumanOrganMapper extends BaseMapper<HumanOrgan> {
    int updateOrganName(HumanOrgan humanOrgan);
}
