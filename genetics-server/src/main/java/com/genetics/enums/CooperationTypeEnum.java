package com.genetics.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 合作方式/收款方枚举
 * 
 * @author 系统生成
 * @date 2024/10/25
 */
@Getter
@AllArgsConstructor
public enum CooperationTypeEnum {

    /**
     * PRO计费
     */
    PRO(0, "PRO"),

    /**
     * 系统AVASK计费
     */
    AVASK(1, "AVASK");

    private final Integer code;
    private final String name;

    /**
     * 根据代码获取枚举
     * 
     * @param code 代码
     * @return 枚举值
     */
    public static CooperationTypeEnum getByCode(Integer code) {
        for (CooperationTypeEnum type : values()) {
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
        CooperationTypeEnum type = getByCode(code);
        return type != null ? type.getName() : null;
    }
}
