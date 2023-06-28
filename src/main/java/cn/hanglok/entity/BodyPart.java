package cn.hanglok.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 身体部位表
 * </p>
 *
 * @author Allen
 * @since 2023-06-07
 */
@Getter
@Setter
@TableName("body_part")
public class BodyPart implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增 id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @Schema(description = "id（新增操作时不填，修改操作时必填）")
    private Long id;

    /**
     * 身体检查部位名称
     */
    @Schema(description = "身体检查部位名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String bodyName;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    private LocalDateTime createdAt;

    /**
     * 修改时间
     */
    @Schema(description = "修改时间")
    private LocalDateTime updatedAt;

    /**
     * 创建人
     */
    @Schema(description = "创建人 id", defaultValue = "-1")
    private Long creator;

    /**
     * 修改人
     */
    @Schema(description = "修改人 id", defaultValue = "-1")
    private Long updater;
}
