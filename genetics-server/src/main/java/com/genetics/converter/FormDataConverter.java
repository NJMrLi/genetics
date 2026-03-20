package com.genetics.converter;

import com.genetics.entity.domain.Company;
import com.genetics.entity.domain.CompanyLegalPerson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;

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
        CLASS_REGISTRY.put("Company", Company.class);
        CLASS_REGISTRY.put("CompanyLegalPerson", CompanyLegalPerson.class);
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
            log.info("【转换成功】[{}] → {}", className, instance);
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
        if (targetType == String.class) return strVal;
        if (targetType == Integer.class || targetType == int.class) return Integer.parseInt(strVal);
        if (targetType == Long.class || targetType == long.class) return Long.parseLong(strVal);
        if (targetType == Double.class || targetType == double.class) return Double.parseDouble(strVal);
        if (targetType == Float.class || targetType == float.class) return Float.parseFloat(strVal);
        if (targetType == Boolean.class || targetType == boolean.class) return Boolean.parseBoolean(strVal);
        if (targetType == BigDecimal.class) return new BigDecimal(strVal);
        return value;
    }

    /**
     * 注册业务实体类（运行时动态扩展）
     */
    public static void registerClass(String key, Class<?> clazz) {
        CLASS_REGISTRY.put(key, clazz);
        log.info("已注册实体类: {} -> {}", key, clazz.getName());
    }
}
