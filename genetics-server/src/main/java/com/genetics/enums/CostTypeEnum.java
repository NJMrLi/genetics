package com.genetics.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 费用类型枚举
 * 
 * @author 系统生成
 * @date 2024/01/01
 */
@Getter
@AllArgsConstructor
public enum CostTypeEnum {

    /**
     * 最低回收费
     */
    MINIMUM_RECOVERY_FEE("MINIMUM_RECOVERY_FEE", "基础回收费"),

    /**
     * 追溯回收费用
     */
    RETROSPECTIVE_RECOVERY_FEE("RETROSPECTIVE_RECOVERY_FEE", "追溯回收费"),

    /**
     * 常规回收费
     */
    REGULAR_RECOVERY_FEE("REGULAR_RECOVERY_FEE", "常规回收费"),

    /**
     * 其他
     */
    OTHER("OTHER", "其他");

    private final String code;
    private final String name;

    /**
     * 根据代码获取枚举
     * 
     * @param code 代码
     * @return 枚举值
     */
    public static CostTypeEnum getByCode(String code) {
        for (CostTypeEnum costType : values()) {
            if (costType.getCode().equals(code)) {
                return costType;
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
        CostTypeEnum costType = getByCode(code);
        return costType != null ? costType.getName() : null;
    }
}
