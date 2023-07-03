package cn.hanglok.service.impl;

import cn.hanglok.dto.ModifyLabelOrganDto;
import cn.hanglok.entity.LabelOrgan;
import cn.hanglok.mapper.LabelOrganMapper;
import cn.hanglok.service.ILabelOrganService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>
 * 标签对应器官表 服务实现类
 * </p>
 *
 * @author Allen
 * @since 2023-06-30
 */
@Service
public class LabelOrganServiceImpl extends ServiceImpl<LabelOrganMapper, LabelOrgan> implements ILabelOrganService {

    private static final Logger logger = LoggerFactory.getLogger(LabelOrganServiceImpl.class);

    @Autowired
    LabelOrganMapper labelOrganMapper;

    @Override
    public int modifyLabelOrgan(ModifyLabelOrganDto modifyLabelOrgan) {
        AtomicInteger success = new AtomicInteger();
        modifyLabelOrgan.getOperates().forEach(op -> {
            switch (op.getOp()) {
                case ADD:
                    try {
                        int insert = labelOrganMapper.insert(new LabelOrgan() {{
                            setLabelId(modifyLabelOrgan.getLabelId());
                            setOrganId(Long.valueOf(op.getOrganId()));
                        }});
                        if (1 == insert) {
                            success.getAndIncrement();
                        }
                    } catch (Exception e) {
                        logger.error("新增标签器官异常：" + op);
                    }
                    break;
                case DEL:
                    int delete = labelOrganMapper.delete(new QueryWrapper<>() {{
                        eq("label_id", modifyLabelOrgan.getLabelId());
                        eq("organ_id", op.getOrganId());
                    }});
                    if (1 == delete) {
                        success.getAndIncrement();
                    }
                    break;
                default:
                    break;

            }
        });

        return success.get();
    }
}
