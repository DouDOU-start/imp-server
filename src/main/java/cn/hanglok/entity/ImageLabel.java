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
 * 标注文件表
 * </p>
 *
 * @author Allen
 * @since 2023-06-07
 */
@Getter
@Setter
@TableName("image_label")
public class ImageLabel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增 id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 器官 id（多选，用英文逗号分隔）
     */
    private String organId;

    /**
     * 文件路径
     */
    private String fileLocation;

    /**
     * 文件存储类型
     */
    private String fileStorageType;

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
