package cn.hanglok.algoSched.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 算法镜像表
 * </p>
 *
 * @author Allen
 * @since 2024-03-01
 */
@Getter
@Setter
public class Images implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增 id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String namespace;

    private String name;

    private String repoFullName;

    private String tag;

    private String resourceUrl;

    private String digest;

    private String repoType;

    private String dateCreate;

    private String label;
}
