package com.genetics.workflow.action;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * 工作流动作执行结果
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkflowActionResult {
    
    /**
     * 是否执行成功
     */
    private boolean success;
    
    /**
     * 新的业务状态ID
     */
    private Integer newStatus;
    
    /**
     * 附加数据(如: 转换后的实体对象)
     */
    private Map<String, Object> data;
    
    /**
     * 结果消息
     */
    private String message;
    
    /**
     * 创建成功结果
     */
    public static WorkflowActionResult success(Integer newStatus, String message) {
        return WorkflowActionResult.builder()
                .success(true)
                .newStatus(newStatus)
                .message(message)
                .build();
    }
    
    /**
     * 创建成功结果(带数据)
     */
    public static WorkflowActionResult success(Integer newStatus, String message, Map<String, Object> data) {
        return WorkflowActionResult.builder()
                .success(true)
                .newStatus(newStatus)
                .message(message)
                .data(data)
                .build();
    }
    
    /**
     * 创建失败结果
     */
    public static WorkflowActionResult fail(String message) {
        return WorkflowActionResult.builder()
                .success(false)
                .message(message)
                .build();
    }
}
