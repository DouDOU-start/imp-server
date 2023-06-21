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
 * 患者表
 * </p>
 *
 * @author Allen
 * @since 2023-06-21
 */
@Getter
@Setter
@TableName("institution_patient")
public class InstitutionPatient implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增 id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 患者 id
     */
    private String patientNumber;

    /**
     * 患者姓名
     */
    private String patientName;

    /**
     * 患者性别
     */
    private String patientSex;

    /**
     * 所属机构 id
     */
    private Long institutionId;

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
