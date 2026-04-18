package com.genetics.entity.workflow;

import lombok.Data;

import java.util.Arrays;
import java.util.List;

/**
 * 模板工作流配置（简化版）
 * 只配置流转规则，状态使用固定的 ServeState 枚举
 */
@Data
public class TemplateWorkflowConfig {

    /**
     * 流转规则列表
     */
    private List<WorkflowTransition> transitions;

    /**
     * 允许终止的状态列表（状态编码列表）
     */
    private List<Integer> allowTerminateFrom;

    /**
     * 获取默认工作流配置（标准流程）
     */
    public static TemplateWorkflowConfig getDefaultConfig() {
        TemplateWorkflowConfig config = new TemplateWorkflowConfig();

        // 默认流转规则
        config.setTransitions(Arrays.asList(
            createTransition(10, 20, "submit", "提交", "📤", false, null),
            createTransition(20, 30, "auditPass", "审核通过", "✅", false, null),
            createTransition(20, 50, "auditReject", "审核驳回", "❌", true, null),
            createTransition(50, 20, "resubmit", "重新提交", "🔄", false, null),
            createTransition(30, 33, "submitLocal", "递交当地同事", "👤", false, null),
            createTransition(33, 32, "submitTax", "递交税局", "🏛️", false, "VAT"),
            createTransition(33, 31, "submitOrg", "递交组织", "🏢", false, "EPR"),
            createTransition(32, 40, "complete", "完成", "🎉", false, null),
            createTransition(31, 40, "complete", "完成", "🎉", false, null)
        ));

        // 允许终止的状态（除已完成、已终止外）
        config.setAllowTerminateFrom(Arrays.asList(10, 20, 30, 31, 32, 33, 50));

        return config;
    }

    /**
     * 获取VAT标准流程配置
     */
    public static TemplateWorkflowConfig getVatConfig() {
        TemplateWorkflowConfig config = new TemplateWorkflowConfig();
        config.setTransitions(Arrays.asList(
            createTransition(10, 20, "submit", "提交", "📤", false, null),
            createTransition(20, 30, "auditPass", "审核通过", "✅", false, null),
            createTransition(20, 50, "auditReject", "审核驳回", "❌", true, null),
            createTransition(50, 20, "resubmit", "重新提交", "🔄", false, null),
            createTransition(30, 33, "submitLocal", "递交当地同事", "👤", false, null),
            createTransition(33, 32, "submitTax", "递交税局", "🏛️", false, "VAT"),
            createTransition(32, 40, "complete", "完成", "🎉", false, null)
        ));
        config.setAllowTerminateFrom(Arrays.asList(10, 20, 30, 32, 33, 50));
        return config;
    }

    /**
     * 获取EPR标准流程配置
     */
    public static TemplateWorkflowConfig getEprConfig() {
        TemplateWorkflowConfig config = new TemplateWorkflowConfig();
        config.setTransitions(Arrays.asList(
            createTransition(10, 20, "submit", "提交", "📤", false, null),
            createTransition(20, 30, "auditPass", "审核通过", "✅", false, null),
            createTransition(20, 50, "auditReject", "审核驳回", "❌", true, null),
            createTransition(50, 20, "resubmit", "重新提交", "🔄", false, null),
            createTransition(30, 33, "submitLocal", "递交当地同事", "👤", false, null),
            createTransition(33, 31, "submitOrg", "递交组织", "🏢", false, "EPR"),
            createTransition(31, 40, "complete", "完成", "🎉", false, null)
        ));
        config.setAllowTerminateFrom(Arrays.asList(10, 20, 30, 31, 33, 50));
        return config;
    }

    private static WorkflowTransition createTransition(Integer from, Integer to, String action,
                                                        String actionName, String icon,
                                                        Boolean needRemark, String condition) {
        WorkflowTransition transition = new WorkflowTransition();
        transition.setFrom(from);
        transition.setTo(to);
        transition.setAction(action);
        transition.setActionName(actionName);
        transition.setIcon(icon);
        transition.setNeedRemark(needRemark);
        transition.setCondition(condition);
        transition.setFormSchema(null); // 默认无动作表单
        return transition;
    }
}
