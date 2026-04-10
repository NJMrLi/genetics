package com.genetics.workflow.action;

import org.pf4j.ExtensionPoint;

/**
 * 工作流动作插件接口
 * 所有动作插件必须实现此接口
 */
public interface WorkflowActionPlugin extends ExtensionPoint {
    
    /**
     * 获取动作编码
     * @return 动作编码 (如: submit, auditPass, auditReject)
     */
    String getActionCode();
    
    /**
     * 获取动作名称
     * @return 动作名称 (如: 提交, 审核通过, 审核驳回)
     */
    String getActionName();
    
    /**
     * 执行动作
     * @param context 动作执行上下文
     * @return 动作执行结果
     */
    WorkflowActionResult execute(WorkflowActionContext context);
    
    /**
     * 是否需要备注
     * @return true-需要备注, false-不需要
     */
    default boolean needRemark() {
        return false;
    }
    
    /**
     * 动作执行前的校验
     * @param context 动作执行上下文
     * @throws IllegalArgumentException 校验失败时抛出异常
     */
    default void validate(WorkflowActionContext context) {
        // 默认不校验,子类可覆盖
    }
}
