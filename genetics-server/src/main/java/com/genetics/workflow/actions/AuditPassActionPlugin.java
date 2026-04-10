package com.genetics.workflow.actions;

import com.genetics.workflow.action.AbstractWorkflowAction;
import com.genetics.workflow.action.WorkflowActionContext;
import com.genetics.workflow.action.WorkflowActionResult;
import lombok.extern.slf4j.Slf4j;
import org.pf4j.Extension;

/**
 * 审核通过动作插件
 */
@Slf4j
@Extension
public class AuditPassActionPlugin extends AbstractWorkflowAction {
    
    @Override
    public String getActionCode() {
        return "auditPass";
    }
    
    @Override
    public String getActionName() {
        return "审核通过";
    }
    
    @Override
    protected WorkflowActionResult doExecute(WorkflowActionContext context) {
        // 审核通过不需要特殊处理,直接返回新状态
        log.info("服务单 {} 审核通过,流转到状态 {}", 
                context.getInstance().getId(), 
                context.getTransition().getTo());
        
        return WorkflowActionResult.success(
                context.getTransition().getTo(),
                "审核通过"
        );
    }
}
