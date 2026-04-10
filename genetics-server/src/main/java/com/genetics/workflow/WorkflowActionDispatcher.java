package com.genetics.workflow;

import com.genetics.common.BusinessException;
import com.genetics.dto.WorkflowTransitionRequestDTO;
import com.genetics.entity.FormInstance;
import com.genetics.entity.workflow.TemplateWorkflowConfig;
import com.genetics.entity.workflow.WorkflowTransition;
import com.genetics.mapper.FormInstanceMapper;
import com.genetics.service.TemplateWorkflowService;
import com.genetics.workflow.action.WorkflowActionContext;
import com.genetics.workflow.action.WorkflowActionPlugin;
import com.genetics.workflow.action.WorkflowActionResult;
import com.genetics.converter.FormDataConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 工作流动作分发器
 * 根据动作编码分发到对应的插件执行
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WorkflowActionDispatcher {

    private final WorkflowPluginManager pluginManager;
    private final TemplateWorkflowService workflowService;
    private final FormInstanceMapper formInstanceMapper;
    private final FormDataConverter formDataConverter;
    private final ObjectMapper objectMapper;

    /**
     * 分发动作到对应插件执行
     */
    @Transactional(rollbackFor = Exception.class)
    public WorkflowActionResult dispatch(Long instanceId, WorkflowTransitionRequestDTO request) {
        String actionCode = request.getAction();
        
        // 1. 查找插件
        WorkflowActionPlugin plugin = pluginManager.getActionPlugin(actionCode);
        if (plugin == null) {
            throw new BusinessException("未知的动作编码: " + actionCode);
        }
        
        // 2. 获取实例
        FormInstance instance = formInstanceMapper.selectById(instanceId);
        if (instance == null) {
            throw new BusinessException("服务单不存在: " + instanceId);
        }
        
        // 3. 构建上下文
        WorkflowActionContext context = buildContext(instance, request);
        
        // 4. 校验流转规则
        if (!workflowService.validateInstanceTransition(instance, actionCode)) {
            throw new BusinessException("当前状态不允许执行此操作");
        }
        
        // 5. 获取流转规则
        WorkflowTransition transition = workflowService.getTransition(
                instance.getTemplateId(), 
                instance.getOrderStatusId(), 
                actionCode
        );
        context.setTransition(transition);
        
        // 6. 插件自定义校验
        plugin.validate(context);
        
        // 7. 执行插件
        WorkflowActionResult result = plugin.execute(context);
        
        if (!result.isSuccess()) {
            throw new BusinessException(result.getMessage());
        }
        
        return result;
    }

    /**
     * 构建动作执行上下文
     */
    private WorkflowActionContext buildContext(FormInstance instance, WorkflowTransitionRequestDTO request) {
        // 获取工作流配置
        TemplateWorkflowConfig config = workflowService.getWorkflowConfig(instance.getTemplateId());
        
        return WorkflowActionContext.builder()
                .instance(instance)
                .remark(request.getRemark())
                .formData(request.getActionFormData())
                .workflowConfig(config)
                .formDataConverter(formDataConverter)
                .objectMapper(objectMapper)
                .build();
    }
}
