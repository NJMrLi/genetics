package com.genetics.service.impl;

import com.genetics.common.BusinessException;
import com.genetics.entity.FormInstance;
import com.genetics.entity.FormTemplate;
import com.genetics.entity.workflow.TemplateWorkflowConfig;
import com.genetics.entity.workflow.WorkflowTransition;
import com.genetics.enums.ServeState;
import com.genetics.service.FormTemplateService;
import com.genetics.service.TemplateWorkflowService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 模板工作流服务实现（简化版）
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TemplateWorkflowServiceImpl implements TemplateWorkflowService {

    private final FormTemplateService formTemplateService;

    @Override
    public TemplateWorkflowConfig getWorkflowConfig(Long templateId) {
        FormTemplate template = formTemplateService.getById(templateId);
        if (template == null) {
            throw new BusinessException("模板不存在");
        }

        // 如果模板有自定义配置则使用，否则使用默认配置
        TemplateWorkflowConfig config = template.getWorkflowConfig();
        if (config == null) {
            config = TemplateWorkflowConfig.getDefaultConfig();
        }
        return config;
    }

    @Override
    public List<WorkflowTransition> getAvailableActions(Long templateId, Integer currentStatus, Map<String, Object> context) {
        TemplateWorkflowConfig config = getWorkflowConfig(templateId);
        List<WorkflowTransition> actions = new ArrayList<>();

        // 添加常规流转操作
        if (config.getTransitions() != null) {
            for (WorkflowTransition transition : config.getTransitions()) {
                if (transition.getFrom().equals(currentStatus)) {
                    // 检查条件（简化：condition为null或匹配serviceType）
                    if (matchCondition(transition.getCondition(), context)) {
                        // 克隆一个 transition 避免直接修改缓存中的对象（如果需要动态处理）
                        WorkflowTransition action = new WorkflowTransition();
                        action.setFrom(transition.getFrom());
                        action.setTo(transition.getTo());
                        action.setAction(transition.getAction());
                        action.setActionName(transition.getActionName());
                        action.setNeedRemark(transition.getNeedRemark());
                        action.setCondition(transition.getCondition());
                        action.setFormSchema(transition.getFormSchema());
                        actions.add(action);
                    }
                }
            }
        }

        // 添加终止操作
        if (config.getAllowTerminateFrom() != null && config.getAllowTerminateFrom().contains(currentStatus)) {
            WorkflowTransition terminate = new WorkflowTransition();
            terminate.setFrom(currentStatus);
            terminate.setTo(99);
            terminate.setAction("terminate");
            terminate.setActionName("终止");
            terminate.setNeedRemark(true);
            actions.add(terminate);
        }

        return actions;
    }

    @Override
    public boolean validateTransition(Long templateId, Integer currentStatus, String action, Map<String, Object> context) {
        TemplateWorkflowConfig config = getWorkflowConfig(templateId);

        // 检查常规流转
        if (config.getTransitions() != null) {
            for (WorkflowTransition transition : config.getTransitions()) {
                if (transition.getFrom().equals(currentStatus)
                        && transition.getAction().equals(action)
                        && matchCondition(transition.getCondition(), context)) {
                    return true;
                }
            }
        }

        // 检查终止操作
        if ("terminate".equals(action) && config.getAllowTerminateFrom() != null) {
            return config.getAllowTerminateFrom().contains(currentStatus);
        }

        return false;
    }

    @Override
    public Integer doTransition(Long templateId, Integer currentStatus, String action, Map<String, Object> context) {
        WorkflowTransition transition = getTransition(templateId, currentStatus, action);
        if (transition == null) {
            throw new BusinessException("当前状态不允许执行此操作");
        }

        // 检查条件
        if (!matchCondition(transition.getCondition(), context)) {
            throw new BusinessException("当前条件不满足，无法执行此操作");
        }

        return transition.getTo();
    }

    @Override
    public WorkflowTransition getTransition(Long templateId, Integer currentStatus, String action) {
        TemplateWorkflowConfig config = getWorkflowConfig(templateId);

        // 查找常规流转
        if (config.getTransitions() != null) {
            for (WorkflowTransition transition : config.getTransitions()) {
                if (transition.getFrom().equals(currentStatus) && transition.getAction().equals(action)) {
                    // 克隆
                    WorkflowTransition t = new WorkflowTransition();
                    t.setFrom(transition.getFrom());
                    t.setTo(transition.getTo());
                    t.setAction(transition.getAction());
                    t.setActionName(transition.getActionName());
                    t.setNeedRemark(transition.getNeedRemark());
                    t.setCondition(transition.getCondition());
                    t.setFormSchema(transition.getFormSchema());
                    return t;
                }
            }
        }

        // 终止操作
        if ("terminate".equals(action) && config.getAllowTerminateFrom() != null
                && config.getAllowTerminateFrom().contains(currentStatus)) {
            WorkflowTransition terminate = new WorkflowTransition();
            terminate.setFrom(currentStatus);
            terminate.setTo(99);
            terminate.setAction("terminate");
            terminate.setActionName("终止");
            terminate.setNeedRemark(true);
            return terminate;
        }

        return null;
    }

    @Override
    public List<WorkflowTransition> getInstanceAvailableActions(FormInstance instance) {
        Map<String, Object> context = buildContext(instance);
        return getAvailableActions(instance.getTemplateId(), instance.getOrderStatusId(), context);
    }

    @Override
    public boolean validateInstanceTransition(FormInstance instance, String action) {
        Map<String, Object> context = buildContext(instance);
        return validateTransition(instance.getTemplateId(), instance.getOrderStatusId(), action, context);
    }

    @Override
    public Integer doInstanceTransition(FormInstance instance, String action) {
        Map<String, Object> context = buildContext(instance);
        return doTransition(instance.getTemplateId(), instance.getOrderStatusId(), action, context);
    }

    /**
     * 匹配条件（简化版）
     * condition: null - 通用，"VAT" - 仅VAT，"EPR" - 仅EPR
     */
    private boolean matchCondition(String condition, Map<String, Object> context) {
        if (condition == null || condition.trim().isEmpty()) {
            return true;
        }
        String serviceType = (String) context.get("serviceType");
        // serviceCodeL1: 01=VAT, 02=EPR
        if ("VAT".equals(condition)) {
            return "01".equals(serviceType);
        }
        if ("EPR".equals(condition)) {
            return "02".equals(serviceType);
        }
        return true;
    }

    /**
     * 构建上下文数据
     */
    private Map<String, Object> buildContext(FormInstance instance) {
        Map<String, Object> context = new HashMap<>();
        if (instance != null) {
            context.put("serviceType", instance.getServiceCodeL1());
            context.put("countryCode", instance.getCountryCode());
            context.put("templateId", instance.getTemplateId());
        }
        return context;
    }
}
