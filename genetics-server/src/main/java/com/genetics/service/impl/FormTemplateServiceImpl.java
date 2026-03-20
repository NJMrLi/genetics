package com.genetics.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.genetics.dto.FormControlDTO;
import com.genetics.dto.FormTemplateDTO;
import com.genetics.dto.FormTemplateDetailVO;
import com.genetics.entity.FormControl;
import com.genetics.entity.FormTemplate;
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
    public void publish(Long id) {
        FormTemplate existing = requireExist(id);
        existing.setStatus(1);
        formTemplateMapper.updateById(existing);
    }

    @Override
    public void delete(Long id) {
        formTemplateMapper.deleteById(id);
    }

    @Override
    public FormTemplateDetailVO getDetailById(Long id) {
        FormTemplate template = requireExist(id);
        return buildDetailVO(template);
    }

    @Override
    public Page<FormTemplate> page(int pageNum, int pageSize, String countryCode, String serviceCodeL3) {
        LambdaQueryWrapper<FormTemplate> wrapper = new LambdaQueryWrapper<FormTemplate>()
                .eq(StringUtils.hasText(countryCode), FormTemplate::getCountryCode, countryCode)
                .eq(StringUtils.hasText(serviceCodeL3), FormTemplate::getServiceCodeL3, serviceCodeL3)
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
        // jsonSchema: Object -> JSON String
        try {
            if (dto.getJsonSchema() instanceof String s) {
                entity.setJsonSchema(s);
            } else {
                entity.setJsonSchema(objectMapper.writeValueAsString(dto.getJsonSchema()));
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

        // 解析 JSON Schema
        try {
            Object schema = objectMapper.readValue(template.getJsonSchema(), Object.class);
            vo.setJsonSchema(schema);

            // 提取所有 controlId
            List<Long> controlIds = extractControlIds(template.getJsonSchema());
            if (!controlIds.isEmpty()) {
                List<FormControl> controls = formControlMapper.selectBatchIds(controlIds);
                List<FormTemplateDetailVO.ControlDetailVO> details = controls.stream()
                        .map(this::toControlDetailVO)
                        .toList();
                vo.setControlDetails(details);
            } else {
                vo.setControlDetails(List.of());
            }
        } catch (JsonProcessingException e) {
            log.warn("解析jsonSchema失败, templateId={}", template.getId(), e);
            vo.setJsonSchema(template.getJsonSchema());
            vo.setControlDetails(List.of());
        }
        return vo;
    }

    @SuppressWarnings("unchecked")
    private List<Long> extractControlIds(String jsonSchema) {
        List<Long> ids = new ArrayList<>();
        try {
            Map<String, Object> schema = objectMapper.readValue(jsonSchema, Map.class);
            List<Map<String, Object>> rows = (List<Map<String, Object>>) schema.get("rows");
            if (rows != null) {
                for (Map<String, Object> row : rows) {
                    List<Map<String, Object>> cells = (List<Map<String, Object>>) row.get("cells");
                    if (cells != null) {
                        for (Map<String, Object> cell : cells) {
                            Object cid = cell.get("controlId");
                            if (cid instanceof Number n) {
                                ids.add(n.longValue());
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
