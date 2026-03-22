package com.genetics.entity.workflow;

import lombok.Data;

/**
 * 工作流状态定义
 */
@Data
public class WorkflowState {
    
    /**
     * 状态编码
     */
    private Integer code;
    
    /**
     * 状态名称
     */
    private String name;
    
    /**
     * 状态类型：initial-初始状态, process-处理中, final-终态, exception-异常态, terminal-终止态
     */
    private String type;
    
    /**
     * 标签类型（用于前端展示颜色）
     */
    private String tagType;
}
