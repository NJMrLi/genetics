package com.genetics.enums;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * EPR计费标准枚举
 *
 * @author AI Assistant
 * @since 2025-10-20
 */
@Schema(description = "EPR计费标准枚举")
public enum EprBillingStandardEnum {

    /**
     * 无计费标准
     */
    NONE(0, "无"),

    /**
     * 新品类新品牌
     */
    NEW_CATEGORY_NEW_BRAND(1, "新品类新品牌"),

    /**
     * 存在的品类品牌
     */
    EXIST_CATEGORY_OR_BRAND(2, "存在的品类品牌");

    private final Integer code;
    private final String description;

    EprBillingStandardEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public Integer getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    /**
     * 根据code获取枚举
     *
     * @param code 编码
     * @return 枚举值
     */
    public static EprBillingStandardEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (EprBillingStandardEnum value : values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

    /**
     * 检查是否为有效的计费标准
     *
     * @param code 编码
     * @return 是否有效
     */
    public static boolean isValid(Integer code) {
        return getByCode(code) != null;
    }

    /**
     * 检查是否需要计费
     *
     * @param code 编码
     * @return 是否需要计费
     */
    public static boolean needsBilling(Integer code) {
        EprBillingStandardEnum standard = getByCode(code);
        return standard != null && standard != NONE;
    }
}
