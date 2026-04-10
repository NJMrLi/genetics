package com.genetics.workflow.action;

import com.genetics.converter.FormDataConverter;
import com.genetics.entity.FormInstance;
import com.genetics.entity.workflow.TemplateWorkflowConfig;
import com.genetics.entity.workflow.WorkflowTransition;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

/**
 * 工作流动作执行上下文
 * 包含动作执行所需的所有数据和工具类
 */
@Data
@Builder
public class WorkflowActionContext {
    
    /**
     * 服务单实例
     */
    private FormInstance instance;
    
    /**
     * 备注信息
     */
    private String remark;
    
    /**
     * 动作触发的表单数据
     */
    private Map<String, Object> formData;
    
    /**
     * 模板工作流配置
     */
    private TemplateWorkflowConfig workflowConfig;
    
    /**
     * 当前流转规则
     */
    private WorkflowTransition transition;
    
    /**
     * 表单数据转换器(由Spring注入)
     */
    private FormDataConverter formDataConverter;
    
    /**
     * JSON处理器(由Spring注入)
     */
    private ObjectMapper objectMapper;
    
    /**
     * 构建上下文工具方法
     */
    public static WorkflowActionContextBuilder builder() {
        return new WorkflowActionContextBuilder();
    }
}
