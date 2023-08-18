package cn.hanglok.pacs.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 研究表
 * </p>
 *
 * @author Allen
 * @since 2023-06-07
 */
@Getter
@Setter
@TableName("image_studies")
public class ImageStudies implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增 id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 研究 id
     */
    private String studyId;

    /**
     * 研究 uid
     */
    private String studyUid;

    /**
     * 研究描述
     */
    private String studyDescription;

    /**
     * 研究时间
     */
    private LocalDateTime studyAt;

    /**
     * 病人 id
     */
    private Long patientId;

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
