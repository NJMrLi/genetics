package com.genetics.converter;

import com.genetics.entity.domain.Company;
import com.genetics.entity.domain.CompanyAddress;
import com.genetics.entity.domain.CompanyAttachment;
import com.genetics.entity.domain.CompanyBank;
import com.genetics.entity.domain.CompanyContact;
import com.genetics.entity.domain.CompanyEprCategoryBrand;
import com.genetics.entity.domain.CompanyOverseaManage;
import com.genetics.entity.domain.CompanyPersonnel;
import com.genetics.entity.domain.CompanySales;
import com.genetics.entity.domain.CompanyTaxNo;
import com.genetics.entity.domain.CompanyTaxNoAttachment;
import com.genetics.entity.domain.CompanyUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 表单数据转换器
 * 将 Map<controlKey, value> 转换为对应业务实体类对象
 * controlKey 格式: ClassName.fieldName
 */
@Slf4j
@Component
public class FormDataConverter {

    /**
     * 已注册的业务实体类
     * 新增实体类时，在此处注册即可
     */
    private static final Map<String, Class<?>> CLASS_REGISTRY = new LinkedHashMap<>();

    static {
        // 公司基本信息
        CLASS_REGISTRY.put("Company", Company.class);
        CLASS_REGISTRY.put("CompanyAddress", CompanyAddress.class);
        CLASS_REGISTRY.put("CompanyAttachment", CompanyAttachment.class);
        CLASS_REGISTRY.put("CompanyBank", CompanyBank.class);
        CLASS_REGISTRY.put("CompanyContact", CompanyContact.class);
        CLASS_REGISTRY.put("CompanyEprCategoryBrand", CompanyEprCategoryBrand.class);
        CLASS_REGISTRY.put("CompanyOverseaManage", CompanyOverseaManage.class);
        CLASS_REGISTRY.put("CompanyPersonnel", CompanyPersonnel.class);
        CLASS_REGISTRY.put("CompanySales", CompanySales.class);
        CLASS_REGISTRY.put("CompanyTaxNo", CompanyTaxNo.class);
        CLASS_REGISTRY.put("CompanyTaxNoAttachment", CompanyTaxNoAttachment.class);
        CLASS_REGISTRY.put("CompanyUser", CompanyUser.class);
    }

    /**
     * 将表单数据转换为实体对象 Map
     *
     * @param formData Map<"ClassName.fieldName", value>
     * @return Map<ClassName, 实体对象>
     */
    public Map<String, Object> convert(Map<String, Object> formData) {
        if (formData == null || formData.isEmpty()) {
            return Collections.emptyMap();
        }

        // 1. 按 ClassName 分组
        Map<String, Map<String, Object>> grouped = new LinkedHashMap<>();
        for (Map.Entry<String, Object> entry : formData.entrySet()) {
            String key = entry.getKey();
            String[] parts = key.split("\\.", 2);
            if (parts.length != 2) {
                log.warn("Invalid controlKey format: '{}', expected ClassName.fieldName, skipping", key);
                continue;
            }
            String className = parts[0];
            String fieldName = parts[1];
            grouped.computeIfAbsent(className, k -> new LinkedHashMap<>())
                    .put(fieldName, entry.getValue());
        }

        // 2. 反射赋值，构建结果
        Map<String, Object> result = new LinkedHashMap<>();
        for (Map.Entry<String, Map<String, Object>> entry : grouped.entrySet()) {
            String className = entry.getKey();
            Class<?> clazz = CLASS_REGISTRY.get(className);
            if (clazz == null) {
                log.warn("No registered class found for key '{}', skipping. " +
                        "Please register it in FormDataConverter.CLASS_REGISTRY", className);
                continue;
            }
            Object instance = createAndPopulate(clazz, entry.getValue());
            result.put(className, instance);
            log.debug("【转换成功】[{}] → {}", className, instance);
        }
        return result;
    }

    /**
     * 反射创建实例并赋值
     */
    private Object createAndPopulate(Class<?> clazz, Map<String, Object> fieldMap) {
        try {
            Object instance = clazz.getDeclaredConstructor().newInstance();
            for (Map.Entry<String, Object> entry : fieldMap.entrySet()) {
                String fieldName = entry.getKey();
                Object value = entry.getValue();
                try {
                    Field field = findField(clazz, fieldName);
                    if (field == null) {
                        log.warn("Field '{}' not found in class '{}'", fieldName, clazz.getSimpleName());
                        continue;
                    }
                    field.setAccessible(true);
                    field.set(instance, convertValue(field.getType(), value));
                } catch (Exception e) {
                    log.warn("赋值失败 [{}.{}]={}, 原因: {}", clazz.getSimpleName(), fieldName, value, e.getMessage());
                }
            }
            return instance;
        } catch (Exception e) {
            log.error("创建实例失败: {}", clazz.getName(), e);
            throw new RuntimeException("对象转换失败: " + clazz.getSimpleName(), e);
        }
    }

    /**
     * 从当前类及父类中查找字段
     */
    private Field findField(Class<?> clazz, String fieldName) {
        Class<?> current = clazz;
        while (current != null && current != Object.class) {
            try {
                return current.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                current = current.getSuperclass();
            }
        }
        return null;
    }

    /**
     * 类型转换：将 JSON 解析出的 Object 转为目标字段类型
     */
    private Object convertValue(Class<?> targetType, Object value) {
        if (value == null) return null;
        if (targetType.isInstance(value)) return value;

        String strVal = value.toString();
        if (strVal.isEmpty()) return null;

        try {
            if (targetType == String.class) return strVal;
            if (targetType == Integer.class || targetType == int.class) return Integer.parseInt(strVal);
            if (targetType == Long.class || targetType == long.class) return Long.parseLong(strVal);
            if (targetType == Double.class || targetType == double.class) return Double.parseDouble(strVal);
            if (targetType == Float.class || targetType == float.class) return Float.parseFloat(strVal);
            if (targetType == Boolean.class || targetType == boolean.class) return Boolean.parseBoolean(strVal);
            if (targetType == BigDecimal.class) return new BigDecimal(strVal);
            if (targetType == LocalDate.class) return LocalDate.parse(strVal);
            if (targetType == LocalDateTime.class) {
                // 支持多种格式
                if (strVal.contains("T")) {
                    return LocalDateTime.parse(strVal);
                } else {
                    return LocalDateTime.parse(strVal, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                }
            }
        } catch (Exception e) {
            log.warn("类型转换失败: {} -> {}, value={}", value.getClass().getSimpleName(), targetType.getSimpleName(), strVal);
        }
        return value;
    }

    /**
     * 注册业务实体类（运行时动态扩展）
     */
    public static void registerClass(String key, Class<?> clazz) {
        CLASS_REGISTRY.put(key, clazz);
        log.info("已注册实体类: {} -> {}", key, clazz.getName());
    }

    /**
     * 获取已注册的类
     */
    public static Class<?> getRegisteredClass(String key) {
        return CLASS_REGISTRY.get(key);
    }

    /**
     * 获取所有已注册的类名
     */
    public static java.util.Set<String> getRegisteredClassNames() {
        return CLASS_REGISTRY.keySet();
    }
}
