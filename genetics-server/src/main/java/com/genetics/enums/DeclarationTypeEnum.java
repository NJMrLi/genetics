package com.genetics.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 申报类型枚举
 * 用于区分不同类型的中报（常规申报、预申报、追溯申报）
 * 
 * @author 系统生成
 * @date 2025/01/15
 */
@Getter
@AllArgsConstructor
public enum DeclarationTypeEnum {

    /**
     * 常规申报
     * 指按照法定周期进行正常申报
     */
    REGULAR(0, "常规申报"),

    /**
     * 预申报
     * 指在正式申报之前进行的预先申报
     */
    PRE_DECLARATION(1, "预申报"),

    /**
     * 追溯申报
     * 指对历史期间进行补申报或追溯申报
     */
    RETROACTIVE(2, "追溯申报");

    private final Integer code;
    private final String name;

    /**
     * 根据代码获取枚举
     * 
     * @param code 代码
     * @return 枚举值
     */
    public static DeclarationTypeEnum getByCode(Integer code) {
        for (DeclarationTypeEnum type : values()) {
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
    public static String getNameByCode(Integer code) {
        DeclarationTypeEnum type = getByCode(code);
        return type != null ? type.getName() : null;
    }

    /**
     * 根据名称获取枚举
     * 
     * @param name 名称
     * @return 枚举值
     */
    public static DeclarationTypeEnum getByName(String name) {
        for (DeclarationTypeEnum type : values()) {
            if (type.getName().equals(name)) {
                return type;
            }
        }
        return null;
    }
}
