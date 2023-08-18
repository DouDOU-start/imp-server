package cn.hanglok.pacs.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 字典表
 * </p>
 *
 * @author Allen
 * @since 2023-07-11
 */
@Getter
@Setter
public class Dictionary implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增 id
     */
    private Integer id;

    /**
     * 字典名
     */
    private String name;

    /**
     * 属性（用英文逗号分隔）
     */
    private String value;

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
