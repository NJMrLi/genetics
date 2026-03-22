package com.genetics.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.genetics.converter.FormDataConverter;
import com.genetics.dto.FormInstanceCreateDTO;
import com.genetics.dto.FormInstanceDetailVO;
import com.genetics.dto.FormInstanceSaveDTO;
import com.genetics.dto.FormTemplateDetailVO;
import com.genetics.dto.WorkflowTransitionRequestDTO;
import com.genetics.entity.FormInstance;
import com.genetics.entity.FormTemplate;
import com.genetics.enums.InstanceStatus;
import com.genetics.enums.ServeState;
import com.genetics.mapper.FormInstanceMapper;
import com.genetics.mapper.FormTemplateMapper;
import com.genetics.service.FormInstanceService;
import com.genetics.service.TemplateWorkflowService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class FormInstanceServiceImpl implements FormInstanceService {

    private final FormInstanceMapper formInstanceMapper;
    private final FormTemplateMapper formTemplateMapper;
    private final FormTemplateServiceImpl formTemplateService;
    private final FormDataConverter formDataConverter;
    private final ObjectMapper objectMapper;
    private final TemplateWorkflowService templateWorkflowService;

    @Override
    public FormInstanceDetailVO create(FormInstanceCreateDTO dto) {
        // 查询模板
        FormTemplate template = formTemplateMapper.selectById(dto.getTemplateId());
        if (template == null) throw new IllegalArgumentException("模板不存在: " + dto.getTemplateId());

        // 创建实例
        FormInstance instance = new FormInstance();
        instance.setTemplateId(template.getId());
        instance.setTemplateName(template.getTemplateName());
        instance.setVersion(template.getVersion());
        instance.setCountryCode(template.getCountryCode());
        instance.setServiceCodeL1(template.getServiceCodeL1());
        instance.setServiceCodeL2(template.getServiceCodeL2());
        instance.setServiceCodeL3(template.getServiceCodeL3());
        instance.setFormData("{}");
        instance.setStatus(InstanceStatus.DRAFT.getCode());
        
        // 初始化为流程配置的第一个状态 (通常是 10-待提交)
        // 建议从 templateWorkflowService 获取初始状态
        instance.setOrderStatusId(ServeState.WAIT_SUBMIT.getId()); 
        formInstanceMapper.insert(instance);

        // 构建详情VO
        FormTemplateDetailVO templateDetail = formTemplateService.buildDetailVO(template);
        return buildDetailVO(instance, templateDetail, new HashMap<>());
    }

    @Override
    public void save(Long id, FormInstanceSaveDTO dto) {
        FormInstance instance = requireExist(id);
        
        // 如果业务状态是 待提交(10) 或 已驳回(50)，则允许保存，即使逻辑状态 status 已经变成了 1(已提交)
        boolean isEditable = instance.getOrderStatusId() == 10 || instance.getOrderStatusId() == 50;
        if (!isEditable && instance.getStatus() > InstanceStatus.DRAFT.getCode()) {
            throw new IllegalArgumentException("当前业务状态不支持修改表单内容");
        }
        
        try {
            instance.setFormData(objectMapper.writeValueAsString(dto.getFormData()));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("表单数据序列化失败", e);
        }
        // 更新业务状态（可选）
        if (dto.getOrderStatusId() != null) {
            instance.setOrderStatusId(dto.getOrderStatusId());
        }
        // 更新服务时间（可选）
        if (dto.getServiceStartTime() != null) {
            instance.setServiceStartTime(dto.getServiceStartTime());
        }
        if (dto.getServiceEndTime() != null) {
            instance.setServiceEndTime(dto.getServiceEndTime());
        }
        formInstanceMapper.updateById(instance);
    }

    @Override
    public Map<String, Object> submit(Long id) {
        FormInstance instance = requireExist(id);
        
        // 允许重新提交：只要业务状态是 待提交(10) 或 已驳回(50) 
        boolean isCanSubmit = instance.getOrderStatusId() == 10 || instance.getOrderStatusId() == 50;
        if (!isCanSubmit && instance.getStatus() >= InstanceStatus.SUBMITTED.getCode()) {
            throw new IllegalArgumentException("当前业务状态不支持提交操作");
        }
        
        // 解析 formData
        Map<String, Object> formData;
        try {
            formData = objectMapper.readValue(instance.getFormData(),
                    new TypeReference<Map<String, Object>>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException("表单数据解析失败", e);
        }

        // 转换为业务实体对象
        Map<String, Object> converted = formDataConverter.convert(formData);

        // TODO: 在此处添加提交后的业务逻辑处理
        // 例如：同步数据到 ERP 系统、发送通知邮件、触发第三方 API 等
        log.info("【业务逻辑TODO】开始处理服务单 {} 的提交后续逻辑...", id);

        // 触发工作流状态流转：提交 (submit)
        Integer newStatus = templateWorkflowService.doInstanceTransition(instance, "submit");
        instance.setOrderStatusId(newStatus);

        // 打印转换结果日志
        converted.forEach((className, obj) -> {
            try {
                log.info("【表单提交转换结果】{}: {}", className, objectMapper.writeValueAsString(obj));
            } catch (JsonProcessingException e) {
                log.info("【表单提交转换结果】{}: {}", className, obj);
            }
        });

        // 更新状态
        instance.setStatus(InstanceStatus.SUBMITTED.getCode());
        instance.setSubmitTime(LocalDateTime.now());
        formInstanceMapper.updateById(instance);

        return converted;
    }

    @Override
    public FormInstanceDetailVO getDetailById(Long id) {
        FormInstance instance = requireExist(id);
        FormTemplate template = formTemplateMapper.selectById(instance.getTemplateId());
        FormTemplateDetailVO templateDetail = formTemplateService.buildDetailVO(template);

        Map<String, Object> formData = parseFormData(instance.getFormData());
        return buildDetailVO(instance, templateDetail, formData);
    }

    @Override
    public void updateOrderStatus(Long id, Integer orderStatusId) {
        ServeState state = ServeState.getServeStateById(orderStatusId);
        if (state == null) {
            throw new IllegalArgumentException("无效的业务状态ID: " + orderStatusId);
        }
        FormInstance instance = requireExist(id);
        instance.setOrderStatusId(orderStatusId);
        formInstanceMapper.updateById(instance);
    }

    @Override
    public void executeTransition(Long id, WorkflowTransitionRequestDTO request) {
        FormInstance instance = requireExist(id);
        String action = request.getAction();
        
        // 使用工作流服务验证并执行状态流转
        Integer newStatus = templateWorkflowService.doInstanceTransition(instance, action);
        
        // 动作相关的表单数据合并 (如果有)
        if (request.getActionFormData() != null && !request.getActionFormData().isEmpty()) {
            Map<String, Object> currentData = parseFormData(instance.getFormData());
            currentData.putAll(request.getActionFormData());
            try {
                instance.setFormData(objectMapper.writeValueAsString(currentData));
            } catch (JsonProcessingException e) {
                log.error("合并表单数据失败", e);
            }
        }

        // 更新状态
        instance.setOrderStatusId(newStatus);
        
        // 如果是提交操作，同时更新提交状态和时间
        if ("submit".equals(action) || "resubmit".equals(action)) {
            instance.setStatus(InstanceStatus.SUBMITTED.getCode());
            instance.setSubmitTime(LocalDateTime.now());
        }
        
        // 如果是驳回操作，重置逻辑状态为草稿
        if ("auditReject".equals(action)) {
            instance.setStatus(InstanceStatus.DRAFT.getCode());
        }
        
        // TODO: 可以添加操作日志记录，记录 remark
        
        formInstanceMapper.updateById(instance);
    }

    @Override
    public Page<FormInstance> page(int pageNum, int pageSize, Integer status, Integer orderStatusId) {
        LambdaQueryWrapper<FormInstance> wrapper = new LambdaQueryWrapper<FormInstance>()
                .eq(status != null, FormInstance::getStatus, status)
                .eq(orderStatusId != null, FormInstance::getOrderStatusId, orderStatusId)
                .orderByDesc(FormInstance::getCreateTime);
        return formInstanceMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
    }

    private FormInstance requireExist(Long id) {
        FormInstance instance = formInstanceMapper.selectById(id);
        if (instance == null) throw new IllegalArgumentException("服务单不存在: " + id);
        return instance;
    }

    private Map<String, Object> parseFormData(String json) {
        try {
            if (json == null || json.isBlank()) return new HashMap<>();
            return objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {});
        } catch (JsonProcessingException e) {
            log.warn("解析formData失败", e);
            return new HashMap<>();
        }
    }

    private FormInstanceDetailVO buildDetailVO(FormInstance instance,
                                               FormTemplateDetailVO templateDetail,
                                               Map<String, Object> formData) {
        FormInstanceDetailVO vo = new FormInstanceDetailVO();
        vo.setInstanceId(instance.getId());
        vo.setTemplateId(instance.getTemplateId());
        vo.setTemplateName(instance.getTemplateName());
        vo.setVersion(instance.getVersion());
        vo.setCountryCode(instance.getCountryCode());
        vo.setServiceCodeL1(instance.getServiceCodeL1());
        vo.setServiceCodeL2(instance.getServiceCodeL2());
        vo.setServiceCodeL3(instance.getServiceCodeL3());
        vo.setJsonSchema(templateDetail.getJsonSchema());
        vo.setControlDetails(templateDetail.getControlDetails());
        vo.setFormData(formData);
        vo.setStatus(instance.getStatus());
        // 业务状态
        int osId = instance.getOrderStatusId() != null ? instance.getOrderStatusId() : ServeState.WAIT_SUBMIT.getId();
        vo.setOrderStatusId(osId);
        vo.setOrderStatusName(ServeState.getName(osId));
        vo.setServiceStartTime(instance.getServiceStartTime());
        vo.setServiceEndTime(instance.getServiceEndTime());
        vo.setSubmitTime(instance.getSubmitTime());
        vo.setCreateTime(instance.getCreateTime());
        return vo;
    }
}
