package com.genetics.entity.workflow;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 工作流动作定义实体
 */
@Data
@TableName("form_workflow_action")
public class WorkflowAction {
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 动作编码 (如 submit, auditPass)
     */
    private String actionCode;

    /**
     * 动作显示名称 (如 提交, 审核通过)
     */
    private String actionName;

    /**
     * 动作图标 (Ionicons 名称)
     */
    private String icon;

    /**
     * 按钮类型 (primary, info, success, warning, error)
     */
    private String buttonType;

    /**
     * 是否默认需要填写备注
     */
    private Boolean needRemark;

    /**
     * 备注框提示语
     */
    private String remarkPlaceholder;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 动作描述
     */
    private String description;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Integer deleted;
}
