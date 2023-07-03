package cn.hanglok.service;

import cn.hanglok.dto.ModifyLabelOrganDto;
import cn.hanglok.entity.LabelOrgan;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 标签对应器官表 服务类
 * </p>
 *
 * @author Allen
 * @since 2023-06-30
 */
public interface ILabelOrganService extends IService<LabelOrgan> {
    int modifyLabelOrgan(ModifyLabelOrganDto modifyLabelOrgan);
}
