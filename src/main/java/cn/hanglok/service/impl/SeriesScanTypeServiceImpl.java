package cn.hanglok.service.impl;

import cn.hanglok.dto.ModifySeriesScanTypeDto;
import cn.hanglok.entity.SeriesScanType;
import cn.hanglok.mapper.SeriesScanTypeMapper;
import cn.hanglok.service.ISeriesScanTypeService;
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
 * 系列对应扫描类型表 服务实现类
 * </p>
 *
 * @author Allen
 * @since 2023-07-17
 */
@Service
public class SeriesScanTypeServiceImpl extends ServiceImpl<SeriesScanTypeMapper, SeriesScanType> implements ISeriesScanTypeService {

    private static final Logger logger = LoggerFactory.getLogger(SeriesScanTypeServiceImpl.class);

    @Autowired
    SeriesScanTypeMapper seriesScanTypeMapper;

    @Override
    public int modifyScanType(ModifySeriesScanTypeDto modifySeriesScanType) {
        AtomicInteger success = new AtomicInteger();
        modifySeriesScanType.getOperates().forEach(op -> {
            switch (op.getOp()) {
                case ADD -> {
                    try {
                        int insert = seriesScanTypeMapper.insert(new SeriesScanType() {{
                            setSeriesId(modifySeriesScanType.getSeriesId());
                            setScanTypeId(Long.valueOf(op.getScanTypeId()));
                            setCreatedAt(LocalDateTime.now());
                            setUpdatedAt(LocalDateTime.now());
                            setCreator(-1L);
                            setUpdater(-1L);
                        }});
                        if (1 == insert) {
                            success.getAndIncrement();
                        }
                    } catch (Exception e) {
                        logger.error("新增系列身体部位异常：" + op + e.getMessage());
                    }
                }
                case DEL -> {
                    int delete = seriesScanTypeMapper.delete(new QueryWrapper<>() {{
                        eq("series_id", modifySeriesScanType.getSeriesId());
                        eq("body_part_id", op.getScanTypeId());
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
