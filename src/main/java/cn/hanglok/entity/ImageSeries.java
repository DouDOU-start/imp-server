package cn.hanglok.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 系列表
 * </p>
 *
 * @author Allen
 * @since 2023-06-12
 */
@Getter
@Setter
@TableName("image_series")
public class ImageSeries implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增 id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 系列号（单个设备唯一）
     */
    private String seriesNumber;

    /**
     * 系列uid
     */
    private String seriesUid;

    /**
     * 系列描述
     */
    private String seriesDescription;

    /**
     * 扫描类型 id
     */
    private Long scanTypeId;

    /**
     * 模态
     */
    private String modality;

    /**
     * 身体部位 id
     */
    private Long bodyPartId;

    /**
     * 像素间距
     */
    private String pixelSpacing;

    /**
     * 切片厚度
     */
    private Object sliceThickness;

    /**
     * 行
     */
    @TableField("`row`")
    private Integer row;

    /**
     * 列
     */
    @TableField("`columns`")
    private Integer columns;

    /**
     * 影像患者年龄
     */
    private String patientAge;

    /**
     * 系列创建时间
     */
    private LocalDateTime seriesAt;

    /**
     * 研究 id
     */
    private Long studyId;

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
    private Long updator;
}
