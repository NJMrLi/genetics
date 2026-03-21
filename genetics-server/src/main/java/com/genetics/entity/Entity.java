package com.genetics.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 实体基类
 */
@Data
public abstract class Entity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 创建人ID */
    @TableField(fill = FieldFill.INSERT)
    private Long createdById;

    /** 创建人名称 */
    @TableField(fill = FieldFill.INSERT)
    private String createdByName;

    /** 最后更新人ID */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long lastUpdatedById;

    /** 最后更新人名称 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String lastUpdatedByName;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    /** 最后更新时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime lastUpdatedAt;

    /** 逻辑删除标记: 0未删除 1已删除 */
    @TableLogic
    @TableField("deleted")
    private Integer deleted;
}
