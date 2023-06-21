package cn.hanglok.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 实例表
 * </p>
 *
 * @author Allen
 * @since 2023-06-07
 */
@Getter
@Setter
@TableName("image_instances")
public class ImageInstances implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增 id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 实例号
     */
    private Integer instanceNumber;

    /**
     * 实例 uid
     */
    private String instanceUid;

    /**
     * 切片位置
     */
    private Object sliceLocation;

    /**
     * 实例创建时间
     */
    private LocalDateTime instanceAt;

    /**
     * 系列 id
     */
    private Long seriesId;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 修改时间
     */
    private LocalDateTime updatedAt;

    /**
     * 创建人
     */
    private Long creator;

    /**
     * 修改人
     */
    private Long updater;
}
