package com.genetics.workflow.actions;

import com.genetics.enums.InstanceStatus;
import com.genetics.workflow.action.AbstractWorkflowAction;
import com.genetics.workflow.action.WorkflowActionContext;
import com.genetics.workflow.action.WorkflowActionResult;
import lombok.extern.slf4j.Slf4j;
import org.pf4j.Extension;

import java.time.LocalDateTime;

/**
 * 重新提交动作插件
 * 驳回后重新提交,逻辑同 submit
 */
@Slf4j
@Extension
public class ResubmitActionPlugin extends AbstractWorkflowAction {
    
    @Override
    public String getActionCode() {
        return "resubmit";
    }
    
    @Override
    public String getActionName() {
        return "重新提交";
    }
    
    @Override
    protected WorkflowActionResult doExecute(WorkflowActionContext context) {
        // 重新提交时更新提交状态和时间
        context.getInstance().setStatus(InstanceStatus.SUBMITTED.getCode());
        context.getInstance().setSubmitTime(LocalDateTime.now());
        
        log.info("服务单 {} 重新提交,流转到状态 {}", 
                context.getInstance().getId(), 
                context.getTransition().getTo());
        
        return WorkflowActionResult.success(
                context.getTransition().getTo(),
                "重新提交成功"
        );
    }
    
    @Override
    public void validate(WorkflowActionContext context) {
        // 重新提交只允许在已驳回状态下执行
        Integer orderStatusId = context.getInstance().getOrderStatusId();
        if (orderStatusId != 50) {
            throw new IllegalArgumentException("只有已驳回的服务单才能重新提交");
        }
    }
}
