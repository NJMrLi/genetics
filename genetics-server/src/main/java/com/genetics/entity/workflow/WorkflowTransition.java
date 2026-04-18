package com.genetics.entity.workflow;

import lombok.Data;

/**
 * 工作流状态流转规则
 */
@Data
public class WorkflowTransition {

    /**
     * 起始状态编码（ServeState的id）
     */
    private Integer from;

    /**
     * 目标状态编码
     */
    private Integer to;

    /**
     * 操作编码
     */
    private String action;

    /**
     * 操作名称
     */
    private String actionName;

    /**
     * 操作图标 (emoji 或 icon 名称)
     */
    private String icon;

    /**
     * 是否需要填写备注/原因
     */
    private Boolean needRemark;

    /**
     * 动作关联的表单配置 (JSON Schema 对象)
     */
    private Object formSchema;

    /**
     * 条件：VAT/EPR/null（为null表示都适用）
     */
    private String condition;
}
