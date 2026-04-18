package com.genetics.workflow.actions;

import com.genetics.workflow.action.AbstractWorkflowAction;
import com.genetics.workflow.action.WorkflowActionContext;
import com.genetics.workflow.action.WorkflowActionResult;
import lombok.extern.slf4j.Slf4j;
import org.pf4j.Extension;

/**
 * 完成动作插件
 */
@Slf4j
@Extension
public class CompleteActionPlugin extends AbstractWorkflowAction {
    
    @Override
    public String getActionCode() {
        return "complete";
    }
    
    @Override
    public String getActionName() {
        return "完成";
    }
    
    @Override
    protected WorkflowActionResult doExecute(WorkflowActionContext context) {
        log.info("服务单 {} 已完成", context.getInstance().getId());
        
        // TODO: 可以添加完成后的逻辑
        // 例如: 生成完成报告、发送完成通知、归档等
        
        return WorkflowActionResult.success(
                context.getTransition().getTo(),
                "服务单已完成"
        );
    }
}
