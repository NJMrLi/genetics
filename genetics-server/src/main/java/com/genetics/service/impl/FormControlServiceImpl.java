package com.genetics.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.genetics.dto.FormControlDTO;
import com.genetics.entity.FormControl;
import com.genetics.mapper.FormControlMapper;
import com.genetics.service.FormControlService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FormControlServiceImpl implements FormControlService {

    private final FormControlMapper formControlMapper;

    @Override
    public Long save(FormControlDTO dto) {
        validateControlKey(dto.getControlKey());
        // 检查key唯一
        long count = formControlMapper.selectCount(
                new LambdaQueryWrapper<FormControl>().eq(FormControl::getControlKey, dto.getControlKey()));
        if (count > 0) {
            throw new IllegalArgumentException("controlKey已存在: " + dto.getControlKey());
        }
        FormControl entity = toEntity(dto);
        if (entity.getEnabled() == null) entity.setEnabled(true);
        if (entity.getSort() == null) entity.setSort(0);
        formControlMapper.insert(entity);
        return entity.getId();
    }

    @Override
    public void update(Long id, FormControlDTO dto) {
        FormControl existing = getById(id);
        // 如果key改变，检查唯一
        if (!existing.getControlKey().equals(dto.getControlKey())) {
            validateControlKey(dto.getControlKey());
            long count = formControlMapper.selectCount(
                    new LambdaQueryWrapper<FormControl>()
                            .eq(FormControl::getControlKey, dto.getControlKey())
                            .ne(FormControl::getId, id));
            if (count > 0) {
                throw new IllegalArgumentException("controlKey已存在: " + dto.getControlKey());
            }
        }
        FormControl entity = toEntity(dto);
        entity.setId(id);
        formControlMapper.updateById(entity);
    }

    @Override
    public void delete(Long id) {
        formControlMapper.deleteById(id);
    }

    @Override
    public FormControl getById(Long id) {
        FormControl control = formControlMapper.selectById(id);
        if (control == null) throw new IllegalArgumentException("控件不存在: " + id);
        return control;
    }

    @Override
    public Page<FormControl> page(int pageNum, int pageSize, String controlType, String businessType, String keyword) {
        LambdaQueryWrapper<FormControl> wrapper = new LambdaQueryWrapper<FormControl>()
                .eq(StringUtils.hasText(controlType), FormControl::getControlType, controlType)
                .eq(StringUtils.hasText(businessType), FormControl::getBusinessType, businessType)
                .and(StringUtils.hasText(keyword), w -> w
                        .like(FormControl::getControlName, keyword)
                        .or().like(FormControl::getControlKey, keyword))
                .orderByAsc(FormControl::getSort);
        return formControlMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
    }

    @Override
    public List<FormControl> listAll() {
        return formControlMapper.selectList(
                new LambdaQueryWrapper<FormControl>()
                        .eq(FormControl::getEnabled, true)
                        .orderByAsc(FormControl::getSort));
    }

    @Override
    public List<FormControl> listByBusinessType(String businessType) {
        return formControlMapper.selectList(
                new LambdaQueryWrapper<FormControl>()
                        .eq(FormControl::getEnabled, true)
                        .eq(StringUtils.hasText(businessType), FormControl::getBusinessType, businessType)
                        .orderByAsc(FormControl::getSort));
    }

    @Override
    public Map<String, List<FormControl>> listGroupedByBusinessType() {
        List<FormControl> allControls = listAll();
        return allControls.stream()
                .filter(c -> c.getBusinessType() != null)
                .collect(Collectors.groupingBy(
                        FormControl::getBusinessType,
                        java.util.LinkedHashMap::new,
                        Collectors.toList()));
    }

    @Override
    public List<String> listAllBusinessTypes() {
        List<FormControl> controls = formControlMapper.selectList(
                new LambdaQueryWrapper<FormControl>()
                        .select(FormControl::getBusinessType)
                        .eq(FormControl::getEnabled, true)
                        .isNotNull(FormControl::getBusinessType)
                        .groupBy(FormControl::getBusinessType)
                        .orderByAsc(FormControl::getBusinessType));
        return controls.stream()
                .map(FormControl::getBusinessType)
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public List<FormControl> listByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) return List.of();
        return formControlMapper.selectBatchIds(ids);
    }

    private void validateControlKey(String controlKey) {
        if (!controlKey.contains(".")) {
            throw new IllegalArgumentException("controlKey格式错误，必须为 ClassName.fieldName 格式");
        }
    }

    private FormControl toEntity(FormControlDTO dto) {
        FormControl entity = new FormControl();
        entity.setControlName(dto.getControlName());
        entity.setControlKey(dto.getControlKey());
        entity.setControlType(dto.getControlType());
        // 业务类型：优先使用传入值，否则从 controlKey 中提取
        String businessType = dto.getBusinessType();
        if (!StringUtils.hasText(businessType) && dto.getControlKey().contains(".")) {
            businessType = dto.getControlKey().split("\\.")[0];
        }
        entity.setBusinessType(businessType);
        entity.setPlaceholder(blankToNull(dto.getPlaceholder()));
        entity.setTips(blankToNull(dto.getTips()));
        entity.setRequired(dto.getRequired());
        entity.setRegexPattern(blankToNull(dto.getRegexPattern()));
        entity.setRegexMessage(blankToNull(dto.getRegexMessage()));
        entity.setMinLength(dto.getMinLength());
        entity.setMaxLength(dto.getMaxLength());
        // JSON 字段：空字符串必须转 null，否则 MySQL 报 Invalid JSON
        entity.setSelectOptions(blankToNull(dto.getSelectOptions()));
        entity.setUploadConfig(blankToNull(dto.getUploadConfig()));
        entity.setDefaultValue(blankToNull(dto.getDefaultValue()));
        entity.setSort(dto.getSort() != null ? dto.getSort() : 0);
        entity.setEnabled(dto.getEnabled() != null ? dto.getEnabled() : true);
        return entity;
    }

    /** 空字符串 / 纯空白 → null */
    private String blankToNull(String s) {
        return StringUtils.hasText(s) ? s : null;
    }
}
