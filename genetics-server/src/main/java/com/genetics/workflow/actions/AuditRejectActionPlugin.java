package com.genetics.workflow.actions;

import com.genetics.enums.InstanceStatus;
import com.genetics.workflow.action.AbstractWorkflowAction;
import com.genetics.workflow.action.WorkflowActionContext;
import com.genetics.workflow.action.WorkflowActionResult;
import lombok.extern.slf4j.Slf4j;
import org.pf4j.Extension;

/**
 * 审核驳回动作插件
 * 驳回时需要重置逻辑状态为草稿,允许重新编辑
 */
@Slf4j
@Extension
public class AuditRejectActionPlugin extends AbstractWorkflowAction {
    
    @Override
    public String getActionCode() {
        return "auditReject";
    }
    
    @Override
    public String getActionName() {
        return "审核驳回";
    }
    
    @Override
    public boolean needRemark() {
        return true; // 驳回必须填写备注
    }
    
    @Override
    protected WorkflowActionResult doExecute(WorkflowActionContext context) {
        // 驳回时重置逻辑状态为草稿
        context.getInstance().setStatus(InstanceStatus.DRAFT.getCode());
        
        log.info("服务单 {} 审核驳回,流转到状态 {}, 原因: {}", 
                context.getInstance().getId(), 
                context.getTransition().getTo(),
                context.getRemark());
        
        return WorkflowActionResult.success(
                context.getTransition().getTo(),
                "审核驳回,请修改后重新提交"
        );
    }
    
    @Override
    public void validate(WorkflowActionContext context) {
        // 驳回操作必须填写备注
        if (context.getRemark() == null || context.getRemark().trim().isEmpty()) {
            throw new IllegalArgumentException("审核驳回必须填写备注原因");
        }
    }
}
