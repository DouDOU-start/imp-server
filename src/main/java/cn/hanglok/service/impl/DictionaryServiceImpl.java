package cn.hanglok.service.impl;

import cn.hanglok.entity.Dictionary;
import cn.hanglok.mapper.DictionaryMapper;
import cn.hanglok.service.IDictionaryService;
import cn.hanglok.service.IImageSeriesService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 字典表 服务实现类
 * </p>
 *
 * @author Allen
 * @since 2023-07-11
 */
@Service
public class DictionaryServiceImpl extends ServiceImpl<DictionaryMapper, Dictionary> implements IDictionaryService {

    @Autowired
    DictionaryMapper dictionaryMapper;

    @Autowired
    IImageSeriesService imageSeriesService;


    @Override
    public synchronized void updateModality() {
        Dictionary modality = dictionaryMapper.selectOne(new QueryWrapper<>() {{
            eq("name", "modality");
        }});
        if (modality == null) {
            save(new Dictionary() {{
                setName("modality");
                setValue(imageSeriesService.getModality().toString());
                setCreator(-1L);
                setUpdater(-1L);
                setCreatedAt(LocalDateTime.now());
                setUpdatedAt(LocalDateTime.now());
            }});
        } else {
            updateById(new Dictionary() {{
                setId(modality.getId());
                setName("modality");
                setValue(Strings.join(imageSeriesService.getModality(), ','));
                setUpdater(99L);
                setUpdatedAt(LocalDateTime.now());
            }});
        }
    }

    @Override
    public List<String> getModality() {
        Dictionary dictionary = dictionaryMapper.selectOne(new QueryWrapper<>() {{
            eq("name", "modality");
        }});
        if (null != dictionary) {
            return List.of(dictionary.getValue().split(","));
        }
        return null;
    }
}
