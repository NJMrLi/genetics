package com.genetics.service;

import com.genetics.entity.FormInstance;
import com.genetics.entity.FormTemplate;
import com.genetics.entity.workflow.TemplateWorkflowConfig;
import com.genetics.entity.workflow.WorkflowTransition;

import java.util.List;
import java.util.Map;

/**
 * 模板工作流服务
 */
public interface TemplateWorkflowService {

    /**
     * 获取模板的工作流配置
     *
     * @param templateId 模板ID
     * @return 工作流配置
     */
    TemplateWorkflowConfig getWorkflowConfig(Long templateId);

    /**
     * 获取当前状态可用的操作列表
     *
     * @param templateId    模板ID
     * @param currentStatus 当前状态编码
     * @param context       上下文数据（用于条件判断）
     * @return 可用操作列表
     */
    List<WorkflowTransition> getAvailableActions(Long templateId, Integer currentStatus, Map<String, Object> context);

    /**
     * 验证状态流转是否合法
     *
     * @param templateId    模板ID
     * @param currentStatus 当前状态
     * @param action        操作编码
     * @param context       上下文数据
     * @return 是否合法
     */
    boolean validateTransition(Long templateId, Integer currentStatus, String action, Map<String, Object> context);

    /**
     * 执行状态流转
     *
     * @param templateId    模板ID
     * @param currentStatus 当前状态
     * @param action        操作编码
     * @param context       上下文数据
     * @return 目标状态编码
     */
    Integer doTransition(Long templateId, Integer currentStatus, String action, Map<String, Object> context);

    /**
     * 获取流转规则
     *
     * @param templateId    模板ID
     * @param currentStatus 当前状态
     * @param action        操作编码
     * @return 流转规则
     */
    WorkflowTransition getTransition(Long templateId, Integer currentStatus, String action);

    /**
     * 获取实例的可用操作（便捷方法）
     *
     * @param instance 服务单实例
     * @return 可用操作列表
     */
    List<WorkflowTransition> getInstanceAvailableActions(FormInstance instance);

    /**
     * 验证实例的状态流转（便捷方法）
     *
     * @param instance 服务单实例
     * @param action   操作编码
     * @return 是否合法
     */
    boolean validateInstanceTransition(FormInstance instance, String action);

    /**
     * 执行实例状态流转（便捷方法）
     *
     * @param instance 服务单实例
     * @param action   操作编码
     * @return 目标状态编码
     */
    Integer doInstanceTransition(FormInstance instance, String action);
}
