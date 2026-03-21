package com.genetics.enums;

import lombok.Getter;

/**
 * EPR模板配置类型枚举
 *
 * @author Kris
 * @since 2024-10-16
 */
@Getter
public enum EprTemplateColumnTypeEnum {

    /**
     * 运营分类
     */
    OPT_CATEGORY("OPT_CATEGORY", "运营分类"),

    /**
     * 分类)"
     */
    CATEGORY("CATEGORY", "分类"),

    /**
     * 指标)"
     */
    INDICATOR("INDICATOR", "指标"),

    /**
     * 计费)"
     */
    FEE("FEE", "计费"),

    /**
     * 亚马逊数据规则
     */
    AMAZON_DATA_RULE("AMAZON_DATA_RULE", "亚马逊数据规则");

    private final String code;
    private final String name;

    EprTemplateColumnTypeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * 根据code获取枚举
     */
    public static EprTemplateColumnTypeEnum fromCode(String code) {
        for (EprTemplateColumnTypeEnum type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }
}
