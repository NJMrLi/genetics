package com.genetics.config;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.genetics.entity.workflow.WorkflowAction;
import com.genetics.service.WorkflowActionService;
import com.genetics.common.constants.WorkflowActionConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 工作流动作初始化
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class WorkflowActionInitializer implements CommandLineRunner {

    private final WorkflowActionService workflowActionService;

    @Override
    public void run(String... args) {
        log.info("开始初始化工作流动作数据...");
        
        List<WorkflowAction> actions = new ArrayList<>();
        
        actions.add(createAction(WorkflowActionConstants.SUBMIT, "提交", "CloudUploadOutline", "primary", false, 10));
        actions.add(createAction(WorkflowActionConstants.AUDIT_PASS, "审核通过", "CheckmarkCircleOutline", "success", false, 20));
        actions.add(createAction(WorkflowActionConstants.AUDIT_REJECT, "审核驳回", "CloseCircleOutline", "error", true, 30));
        actions.add(createAction(WorkflowActionConstants.RESUBMIT, "重新提交", "RefreshOutline", "primary", false, 40));
        actions.add(createAction(WorkflowActionConstants.SUBMIT_LOCAL, "递交当地同事", "PeopleOutline", "info", false, 50));
        actions.add(createAction(WorkflowActionConstants.SUBMIT_TAX, "递交税局", "BusinessOutline", "info", false, 60));
        actions.add(createAction(WorkflowActionConstants.SUBMIT_ORG, "递交组织", "FileTrayFullOutline", "info", false, 70));
        actions.add(createAction(WorkflowActionConstants.COMPLETE, "完成", "CheckmarkDoneOutline", "success", false, 80));
        actions.add(createAction(WorkflowActionConstants.TERMINATE, "终止", "StopCircleOutline", "error", true, 90));

        for (WorkflowAction action : actions) {
            long count = workflowActionService.count(new LambdaQueryWrapper<WorkflowAction>()
                    .eq(WorkflowAction::getActionCode, action.getActionCode()));
            
            if (count == 0) {
                workflowActionService.save(action);
                log.info("已初始化动作: {} ({})", action.getActionName(), action.getActionCode());
            }
        }
        
        log.info("工作流动作数据初始化完成");
    }

    private WorkflowAction createAction(String code, String name, String icon, String buttonType, boolean needRemark, int sort) {
        WorkflowAction action = new WorkflowAction();
        action.setActionCode(code);
        action.setActionName(name);
        action.setIcon(icon);
        action.setButtonType(buttonType);
        action.setNeedRemark(needRemark);
        action.setSort(sort);
        return action;
    }
}
