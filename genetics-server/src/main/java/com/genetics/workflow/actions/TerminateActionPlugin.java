package com.genetics.workflow.actions;

import com.genetics.enums.InstanceStatus;
import com.genetics.workflow.action.AbstractWorkflowAction;
import com.genetics.workflow.action.WorkflowActionContext;
import com.genetics.workflow.action.WorkflowActionResult;
import lombok.extern.slf4j.Slf4j;
import org.pf4j.Extension;

/**
 * 终止动作插件
 */
@Slf4j
@Extension
public class TerminateActionPlugin extends AbstractWorkflowAction {
    
    @Override
    public String getActionCode() {
        return "terminate";
    }
    
    @Override
    public String getActionName() {
        return "终止";
    }
    
    @Override
    public boolean needRemark() {
        return true; // 终止必须填写原因
    }
    
    @Override
    protected WorkflowActionResult doExecute(WorkflowActionContext context) {
        // 终止时更新逻辑状态为草稿(已终止)
        context.getInstance().setStatus(InstanceStatus.DRAFT.getCode());
        
        log.info("服务单 {} 已终止, 原因: {}", 
                context.getInstance().getId(), 
                context.getRemark());
        
        // TODO: 可以添加终止后的逻辑
        // 例如: 发送终止通知、清理资源等
        
        return WorkflowActionResult.success(
                context.getTransition().getTo(),
                "服务单已终止"
        );
    }
    
    @Override
    public void validate(WorkflowActionContext context) {
        // 终止操作必须填写备注
        if (context.getRemark() == null || context.getRemark().trim().isEmpty()) {
            throw new IllegalArgumentException("终止服务单必须填写原因");
        }
    }
}
