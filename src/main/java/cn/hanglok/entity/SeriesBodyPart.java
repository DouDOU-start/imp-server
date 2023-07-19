package cn.hanglok.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 * 系列对应身体部位表
 * </p>
 *
 * @author Allen
 * @since 2023-07-13
 */
@Getter
@Setter
@TableName("series_body_part")
public class SeriesBodyPart implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增 id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 系列 id
     */
    private Long seriesId;

    /**
     * 身体检查部位 id
     */
    private Long bodyPartId;
}