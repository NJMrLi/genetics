package com.genetics.workflow.action;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * 工作流动作抽象基类
 * 提供通用的工具方法和日志记录
 */
@Slf4j
public abstract class AbstractWorkflowAction implements WorkflowActionPlugin {
    
    @Override
    public WorkflowActionResult execute(WorkflowActionContext context) {
        try {
            log.info("开始执行动作: {} - {}", getActionCode(), getActionName());
            
            // 执行前校验
            validate(context);
            
            // 执行具体逻辑
            WorkflowActionResult result = doExecute(context);
            
            if (result.isSuccess()) {
                log.info("动作执行成功: {} -> 状态 {}", getActionCode(), result.getNewStatus());
            } else {
                log.warn("动作执行失败: {} - {}", getActionCode(), result.getMessage());
            }
            
            return result;
            
        } catch (Exception e) {
            log.error("动作执行异常: {}", getActionCode(), e);
            return WorkflowActionResult.fail("动作执行失败: " + e.getMessage());
        }
    }
    
    /**
     * 子类实现具体的执行逻辑
     */
    protected abstract WorkflowActionResult doExecute(WorkflowActionContext context);
    
    /**
     * 解析表单数据
     */
    protected Map<String, Object> parseFormData(String formDataJson, ObjectMapper objectMapper) {
        try {
            if (formDataJson == null || formDataJson.isBlank()) {
                return new HashMap<>();
            }
            return objectMapper.readValue(formDataJson, new TypeReference<Map<String, Object>>() {});
        } catch (JsonProcessingException e) {
            log.warn("解析表单数据失败", e);
            return new HashMap<>();
        }
    }
    
    /**
     * 合并表单数据
     */
    protected String mergeFormData(String existingJson, Map<String, Object> newData, ObjectMapper objectMapper) {
        try {
            Map<String, Object> existing = parseFormData(existingJson, objectMapper);
            existing.putAll(newData);
            return objectMapper.writeValueAsString(existing);
        } catch (JsonProcessingException e) {
            log.error("合并表单数据失败", e);
            return existingJson;
        }
    }
}
