package com.genetics.workflow.actions;

import com.genetics.workflow.action.AbstractWorkflowAction;
import com.genetics.workflow.action.WorkflowActionContext;
import com.genetics.workflow.action.WorkflowActionResult;
import lombok.extern.slf4j.Slf4j;
import org.pf4j.Extension;

/**
 * 递交税局动作插件 (VAT流程)
 */
@Slf4j
@Extension
public class SubmitTaxActionPlugin extends AbstractWorkflowAction {
    
    @Override
    public String getActionCode() {
        return "submitTax";
    }
    
    @Override
    public String getActionName() {
        return "递交税局";
    }
    
    @Override
    protected WorkflowActionResult doExecute(WorkflowActionContext context) {
        log.info("服务单 {} 已递交税局处理", context.getInstance().getId());
        
        // TODO: 可以添加与税局系统对接的逻辑
        // 例如: 调用税局API、上传申报数据等
        
        return WorkflowActionResult.success(
                context.getTransition().getTo(),
                "已递交税局"
        );
    }
}
