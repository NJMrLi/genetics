package com.genetics.enums;

import lombok.Getter;

@Getter
public enum DeclarationCycleType {

    QUARTER(1, "自然季度"),
    MONTH(2, "月度"),
    YEAR(3, "年度"),
    NON_NATURAL_QUARTER(4, "非自然季度"), // 英国，申报起止月可能为任意月，且首次申报可能不止3个月
    DOUBLE_MONTH(5, "双月");

    private final Integer id;
    private final String name;

    DeclarationCycleType(int id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * 根据中文名称返回枚举，找不到返回 null
     */
    public static DeclarationCycleType getTypeByName(String name) {
        for (DeclarationCycleType t : values()) {
            if (t.name.equals(name)) {
                return t;
            }
        }
        return null;
    }

    public static DeclarationCycleType getTypeById(Integer id) {
        for (DeclarationCycleType t : values()) {
            if (t.getId().equals(id)) {
                return t;
            }
        }
        return null;
    }

    /**
     * 根据申报周期ID和国家代码获取显示名称
     */
    public static String getDisplayName(Integer cycleId) {
        DeclarationCycleType type = getTypeById(cycleId);
        if (type == null) {
            return "未知";
        }
        return type.getName();
    }

}
