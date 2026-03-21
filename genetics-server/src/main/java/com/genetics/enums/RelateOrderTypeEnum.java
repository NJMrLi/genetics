package com.genetics.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 关联类型枚举
 * 
 * @author 系统生成
 * @date 2024/10/25
 */
@Getter
@AllArgsConstructor
public enum RelateOrderTypeEnum {

    /**
     * 服务单号
     */
    SERVE_LIST("0", "服务单号"),

    /**
     * 申报单号
     */
    DECLARATION_ORDER("1", "申报单号");

    private final String code;
    private final String name;

    /**
     * 根据代码获取枚举
     * 
     * @param code 代码
     * @return 枚举值
     */
    public static RelateOrderTypeEnum getByCode(String code) {
        for (RelateOrderTypeEnum type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }

    /**
     * 根据代码获取名称
     * 
     * @param code 代码
     * @return 名称
     */
    public static String getNameByCode(String code) {
        RelateOrderTypeEnum type = getByCode(code);
        return type != null ? type.getName() : null;
    }
}
