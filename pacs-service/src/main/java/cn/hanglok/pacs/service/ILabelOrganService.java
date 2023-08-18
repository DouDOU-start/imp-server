package cn.hanglok.pacs.service;

import cn.hanglok.pacs.dto.ModifyLabelOrganDto;
import cn.hanglok.pacs.entity.LabelOrgan;
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
