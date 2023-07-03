package cn.hanglok.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 标签对应器官表
 * </p>
 *
 * @author Allen
 * @since 2023-06-30
 */
@Getter
@Setter
@TableName("label_organ")
public class LabelOrgan implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增 id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 标签 id
     */
    private Long labelId;

    /**
     * 器官 id
     */
    private Long organId;
}
