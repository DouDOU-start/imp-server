package cn.hanglok.pacs.service.impl;

import cn.hanglok.pacs.dto.ModifySeriesBodyPartDto;
import cn.hanglok.pacs.entity.SeriesBodyPart;
import cn.hanglok.pacs.mapper.SeriesBodyPartMapper;
import cn.hanglok.pacs.service.ISeriesBodyPartService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>
 * 系列对应身体部位表 服务实现类
 * </p>
 *
 * @author Allen
 * @since 2023-07-13
 */
@Service
public class SeriesBodyPartServiceImpl extends ServiceImpl<SeriesBodyPartMapper, SeriesBodyPart> implements ISeriesBodyPartService {


    private static final Logger logger = LoggerFactory.getLogger(SeriesBodyPartServiceImpl.class);

    @Autowired
    SeriesBodyPartMapper seriesBodyPartMapper;

    @Override
    public int modifyBodyPart(ModifySeriesBodyPartDto modifySeriesBodyPart) {
        AtomicInteger success = new AtomicInteger();
        modifySeriesBodyPart.getOperates().forEach(op -> {
            switch (op.getOp()) {
                case ADD -> {
                    try {
                        int insert = seriesBodyPartMapper.insert(new SeriesBodyPart() {{
                            setSeriesId(modifySeriesBodyPart.getSeriesId());
                            setBodyPartId(Long.valueOf(op.getBodyPartId()));
                            setCreatedAt(LocalDateTime.now());
                            setUpdatedAt(LocalDateTime.now());
                        }});
                        if (1 == insert) {
                            success.getAndIncrement();
                        }
                    } catch (Exception e) {
                        logger.error("新增系列身体部位异常：" + op);
                    }
                }
                case DEL -> {
                    int delete = seriesBodyPartMapper.delete(new QueryWrapper<>() {{
                        eq("series_id", modifySeriesBodyPart.getSeriesId());
                        eq("body_part_id", op.getBodyPartId());
                    }});
                    if (1 == delete) {
                        success.getAndIncrement();
                    }
                }
                default -> {
                }
            }
        });

        return success.get();
    }
}
