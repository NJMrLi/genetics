package com.genetics.workflow.actions;

import com.genetics.workflow.action.AbstractWorkflowAction;
import com.genetics.workflow.action.WorkflowActionContext;
import com.genetics.workflow.action.WorkflowActionResult;
import lombok.extern.slf4j.Slf4j;
import org.pf4j.Extension;

/**
 * 递交组织动作插件 (EPR流程)
 */
@Slf4j
@Extension
public class SubmitOrgActionPlugin extends AbstractWorkflowAction {
    
    @Override
    public String getActionCode() {
        return "submitOrg";
    }
    
    @Override
    public String getActionName() {
        return "递交组织";
    }
    
    @Override
    protected WorkflowActionResult doExecute(WorkflowActionContext context) {
        log.info("服务单 {} 已递交组织处理", context.getInstance().getId());
        
        // TODO: 可以添加与EPR组织系统对接的逻辑
        // 例如: 调用EPR组织API、提交注册数据等
        
        return WorkflowActionResult.success(
                context.getTransition().getTo(),
                "已递交组织"
        );
    }
}
