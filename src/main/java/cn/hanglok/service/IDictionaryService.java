package cn.hanglok.service;

import cn.hanglok.entity.Dictionary;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 字典表 服务类
 * </p>
 *
 * @author Allen
 * @since 2023-07-11
 */
public interface IDictionaryService extends IService<Dictionary> {
    void updateModality();

    List<String> getModality();
}
