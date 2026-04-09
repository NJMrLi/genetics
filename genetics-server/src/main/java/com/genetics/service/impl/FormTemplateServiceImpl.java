package com.genetics.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.genetics.dto.FormControlDTO;
import com.genetics.dto.FormTemplateDTO;
import com.genetics.dto.FormTemplateDetailVO;
import com.genetics.entity.FormControl;
import com.genetics.entity.FormTemplate;
import com.genetics.entity.workflow.WorkflowTransition;
import com.genetics.mapper.FormControlMapper;
import com.genetics.mapper.FormTemplateMapper;
import com.genetics.service.FormTemplateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class FormTemplateServiceImpl implements FormTemplateService {

    private final FormTemplateMapper formTemplateMapper;
    private final FormControlMapper formControlMapper;
    private final ObjectMapper objectMapper;

    @Override
    public Long save(FormTemplateDTO dto) {
        FormTemplate entity = toEntity(dto);
        entity.setStatus(dto.getStatus() != null ? dto.getStatus() : 0);
        formTemplateMapper.insert(entity);
        return entity.getId();
    }

    @Override
    public void update(Long id, FormTemplateDTO dto) {
        FormTemplate existing = requireExist(id);
        if (existing.getStatus() != null && existing.getStatus() == 1) {
            throw new IllegalArgumentException("已发布的模板不可修改，请升级版本");
        }
        FormTemplate entity = toEntity(dto);
        entity.setId(id);
        formTemplateMapper.updateById(entity);
    }

    @Override
    public FormTemplate getById(Long id) {
        return formTemplateMapper.selectById(id);
    }

    @Override
    public void publish(Long id) {
        FormTemplate existing = requireExist(id);
        
        // 当发布一个新版本时，需要将同一业务下的旧版本标记为已弃用(status=2)
        LambdaUpdateWrapper<FormTemplate> updateWrapper = new LambdaUpdateWrapper<FormTemplate>()
                .eq(FormTemplate::getTemplateName, existing.getTemplateName())
                .eq(FormTemplate::getCountryCode, existing.getCountryCode())
                .eq(FormTemplate::getServiceCodeL3, existing.getServiceCodeL3())
                .set(FormTemplate::getStatus, 2) // 设为已弃用/归档
                .ne(FormTemplate::getId, id)     // 排除当前正在发布的 ID
                .eq(FormTemplate::getStatus, 1); // 只处理当前已发布的
        
        formTemplateMapper.update(null, updateWrapper);
        
        existing.setStatus(1); // 设为已发布
        formTemplateMapper.updateById(existing);
    }

    @Override
    public void delete(Long id) {
        formTemplateMapper.deleteById(id);
    }

    @Override
    public Long upgrade(Long id) {
        FormTemplate existing = requireExist(id);
        
        // 复制基本信息，增加版本号，状态设为草稿
        FormTemplate newVersion = new FormTemplate();
        newVersion.setTemplateName(existing.getTemplateName());
        newVersion.setCountryCode(existing.getCountryCode());
        newVersion.setServiceCodeL1(existing.getServiceCodeL1());
        newVersion.setServiceCodeL2(existing.getServiceCodeL2());
        newVersion.setServiceCodeL3(existing.getServiceCodeL3());
        newVersion.setJsonSchema(existing.getJsonSchema());
        newVersion.setWorkflowConfig(existing.getWorkflowConfig());
        newVersion.setRemark(existing.getRemark());
        newVersion.setStatus(0); // 新版本初始为草稿
        
        // 版本号自增逻辑 (简单加1)
        String oldVersion = existing.getVersion();
        String nextVersion = incrementVersion(oldVersion);
        newVersion.setVersion(nextVersion);
        
        formTemplateMapper.insert(newVersion);
        
        // 升级后原版本保持原样（直到新版本发布才会标记弃用），
        // 这样可以保证在新版本编辑期间，老版本仍然可用。
        return newVersion.getId();
    }

    private String incrementVersion(String version) {
        if (version == null || !version.contains(".")) return "1.0.0";
        try {
            String[] parts = version.split("\\.");
            int last = Integer.parseInt(parts[parts.length - 1]);
            parts[parts.length - 1] = String.valueOf(last + 1);
            return String.join(".", parts);
        } catch (Exception e) {
            return version + ".1";
        }
    }

    @Override
    public FormTemplateDetailVO getDetailById(Long id) {
        FormTemplate template = requireExist(id);
        return buildDetailVO(template);
    }

    @Override
    public Page<FormTemplate> page(int pageNum, int pageSize, String countryCode, String serviceCodeL3, Integer status, Boolean latestOnly) {
        LambdaQueryWrapper<FormTemplate> wrapper = new LambdaQueryWrapper<FormTemplate>()
                .eq(StringUtils.hasText(countryCode), FormTemplate::getCountryCode, countryCode)
                .eq(StringUtils.hasText(serviceCodeL3), FormTemplate::getServiceCodeL3, serviceCodeL3)
                .eq(status != null, FormTemplate::getStatus, status)
                .orderByDesc(FormTemplate::getCreateTime);
        return formTemplateMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
    }

    private FormTemplate requireExist(Long id) {
        FormTemplate template = formTemplateMapper.selectById(id);
        if (template == null) throw new IllegalArgumentException("模板不存在: " + id);
        return template;
    }

    private FormTemplate toEntity(FormTemplateDTO dto) {
        FormTemplate entity = new FormTemplate();
        entity.setTemplateName(dto.getTemplateName());
        entity.setVersion(StringUtils.hasText(dto.getVersion()) ? dto.getVersion() : "1.0.0");
        entity.setCountryCode(dto.getCountryCode());
        entity.setServiceCodeL1(dto.getServiceCodeL1());
        entity.setServiceCodeL2(dto.getServiceCodeL2());
        entity.setServiceCodeL3(dto.getServiceCodeL3());
        entity.setRemark(dto.getRemark());
        // workflowConfig (使用 TableField(typeHandler = JacksonTypeHandler.class) 处理，直接设置对象)
        entity.setWorkflowConfig(dto.getWorkflowConfig());
        // jsonSchema: Object -> JSON String
        try {
            Object schema = dto.getJsonSchema();
            if (schema == null) {
                entity.setJsonSchema(null);
            } else if (schema instanceof String) {
                // 如果是字符串且包含 rows，可能是前端传来的 JSON 字符串，直接保存
                // 否则，如果是一个非 JSON 格式的字符串，jackson 会自动加上双引号，导致双重编码
                String strSchema = (String) schema;
                if (strSchema.startsWith("{") && strSchema.contains("\"rows\"")) {
                    entity.setJsonSchema(strSchema);
                } else {
                    entity.setJsonSchema(objectMapper.writeValueAsString(schema));
                }
            } else {
                entity.setJsonSchema(objectMapper.writeValueAsString(schema));
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException("jsonSchema序列化失败", e);
        }
        return entity;
    }

    /**
     * 构建模板详情VO，从 jsonSchema 中提取所有 controlId，查询控件详情
     */
    FormTemplateDetailVO buildDetailVO(FormTemplate template) {
        if (template == null) return null;
        FormTemplateDetailVO vo = new FormTemplateDetailVO();
        vo.setId(template.getId());
        vo.setTemplateName(template.getTemplateName());
        vo.setVersion(template.getVersion());
        vo.setCountryCode(template.getCountryCode());
        vo.setServiceCodeL1(template.getServiceCodeL1());
        vo.setServiceCodeL2(template.getServiceCodeL2());
        vo.setServiceCodeL3(template.getServiceCodeL3());
        vo.setStatus(template.getStatus());
        vo.setRemark(template.getRemark());
        vo.setWorkflowConfig(template.getWorkflowConfig());

        // 解析 JSON Schema
        if (StringUtils.hasText(template.getJsonSchema())) {
            try {
                Object schema = objectMapper.readValue(template.getJsonSchema(), Object.class);
                vo.setJsonSchema(schema);

                // 提取所有 controlId (包含基础表单和动作表单)
                Set<Long> controlIds = new HashSet<>(extractControlIds(template.getJsonSchema()));
                if (template.getWorkflowConfig() != null && template.getWorkflowConfig().getTransitions() != null) {
                    for (WorkflowTransition transition : template.getWorkflowConfig().getTransitions()) {
                        if (transition.getFormSchema() != null) {
                            String actionSchema;
                            if (transition.getFormSchema() instanceof String) {
                                actionSchema = (String) transition.getFormSchema();
                            } else {
                                actionSchema = objectMapper.writeValueAsString(transition.getFormSchema());
                            }
                            controlIds.addAll(extractControlIds(actionSchema));
                        }
                    }
                }

                if (!controlIds.isEmpty()) {
                    List<FormControl> controls = formControlMapper.selectBatchIds(controlIds);
                    List<FormTemplateDetailVO.ControlDetailVO> details = controls.stream()
                            .map(this::toControlDetailVO)
                            .toList();
                    vo.setControlDetails(details);
                } else {
                    vo.setControlDetails(List.of());
                }
            } catch (Exception e) {
                log.warn("解析jsonSchema或提取控件失败, templateId={}", template.getId(), e);
                vo.setJsonSchema(template.getJsonSchema());
                vo.setControlDetails(List.of());
            }
        } else {
            vo.setJsonSchema(null);
            vo.setControlDetails(List.of());
        }
        return vo;
    }

    @SuppressWarnings("unchecked")
    private List<Long> extractControlIds(String jsonSchema) {
        List<Long> ids = new ArrayList<>();
        if (!StringUtils.hasText(jsonSchema)) {
            return ids;
        }
        try {
            Map<String, Object> schema = objectMapper.readValue(jsonSchema, Map.class);
            if (schema == null) {
                return ids;
            }
            List<Map<String, Object>> rows = (List<Map<String, Object>>) schema.get("rows");
            if (rows != null) {
                for (Map<String, Object> row : rows) {
                    List<Map<String, Object>> cells = (List<Map<String, Object>>) row.get("cells");
                    if (cells != null) {
                        for (Map<String, Object> cell : cells) {
                            Object cid = cell.get("controlId");
                            if (cid instanceof Number) {
                                ids.add(((Number) cid).longValue());
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.warn("提取controlIds失败", e);
        }
        return ids;
    }

    private FormTemplateDetailVO.ControlDetailVO toControlDetailVO(FormControl control) {
        FormTemplateDetailVO.ControlDetailVO vo = new FormTemplateDetailVO.ControlDetailVO();
        vo.setControlId(control.getId());
        vo.setControlKey(control.getControlKey());
        vo.setControlType(control.getControlType());
        vo.setControlName(control.getControlName());
        vo.setPlaceholder(control.getPlaceholder());
        vo.setTips(control.getTips());
        vo.setRequired(control.getRequired());
        vo.setRegexPattern(control.getRegexPattern());
        vo.setRegexMessage(control.getRegexMessage());
        vo.setMinLength(control.getMinLength());
        vo.setMaxLength(control.getMaxLength());
        vo.setDefaultValue(control.getDefaultValue());
        // 解析JSON字段
        try {
            if (StringUtils.hasText(control.getSelectOptions())) {
                vo.setSelectOptions(objectMapper.readValue(control.getSelectOptions(), Object.class));
            }
            if (StringUtils.hasText(control.getUploadConfig())) {
                vo.setUploadConfig(objectMapper.readValue(control.getUploadConfig(), Object.class));
            }
        } catch (JsonProcessingException e) {
            log.warn("解析控件JSON字段失败, controlId={}", control.getId(), e);
        }
        return vo;
    }
}
