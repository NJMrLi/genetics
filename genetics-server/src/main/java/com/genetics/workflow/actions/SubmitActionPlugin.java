package com.genetics.workflow.actions;

import com.genetics.converter.FormDataConverter;
import com.genetics.enums.InstanceStatus;
import com.genetics.workflow.action.AbstractWorkflowAction;
import com.genetics.workflow.action.WorkflowActionContext;
import com.genetics.workflow.action.WorkflowActionResult;
import lombok.extern.slf4j.Slf4j;
import org.pf4j.Extension;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 提交动作插件
 * 将表单数据转换为业务实体,并流转到下一状态
 */
@Slf4j
@Extension
public class SubmitActionPlugin extends AbstractWorkflowAction {
    
    @Override
    public String getActionCode() {
        return "submit";
    }
    
    @Override
    public String getActionName() {
        return "提交";
    }
    
    @Override
    protected WorkflowActionResult doExecute(WorkflowActionContext context) {
        FormDataConverter converter = context.getFormDataConverter();
        
        // 解析 formData
        Map<String, Object> formData = parseFormData(
                context.getInstance().getFormData(), 
                context.getObjectMapper()
        );
        
        // 转换为业务实体对象
        Map<String, Object> converted = converter.convert(formData);
        
        // 打印转换结果日志
        converted.forEach((className, obj) -> {
            try {
                log.info("【表单提交转换结果】{}: {}", className, 
                        context.getObjectMapper().writeValueAsString(obj));
            } catch (Exception e) {
                log.info("【表单提交转换结果】{}: {}", className, obj);
            }
        });
        
        // TODO: 在此处添加提交后的业务逻辑处理
        // 例如：同步数据到 ERP 系统、发送通知邮件、触发第三方 API 等
        log.info("【业务逻辑TODO】开始处理服务单 {} 的提交后续逻辑...", context.getInstance().getId());
        
        // 更新实例状态
        context.getInstance().setStatus(InstanceStatus.SUBMITTED.getCode());
        context.getInstance().setSubmitTime(LocalDateTime.now());
        
        // 返回结果(包含转换后的实体数据)
        return WorkflowActionResult.success(
                context.getTransition().getTo(),
                "提交成功",
                converted
        );
    }
    
    @Override
    public void validate(WorkflowActionContext context) {
        // 检查业务状态是否允许提交
        Integer orderStatusId = context.getInstance().getOrderStatusId();
        boolean canSubmit = orderStatusId == 10 || orderStatusId == 50; // 待提交 或 已驳回
        
        if (!canSubmit) {
            throw new IllegalArgumentException("当前业务状态不支持提交操作");
        }
    }
}
