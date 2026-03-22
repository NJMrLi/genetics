package com.genetics.dto;

import lombok.Data;
import java.util.Map;

/**
 * 执行状态流转请求DTO
 */
@Data
public class WorkflowTransitionRequestDTO {
    /**
     * 操作编码 (submit, auditPass, etc.)
     */
    private String action;

    /**
     * 备注/原因
     */
    private String remark;

    /**
     * 动作触发时填写的额外表单数据
     */
    private Map<String, Object> actionFormData;
}
