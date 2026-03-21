package com.genetics.enums;

public enum ServicePeriodType {

    ONE_YEAR(1, "1年"),
    CALENDAR_YEAR(2, "自然年"),
    SINGLE_TIME(3, "单次");

    private final int id;
    private final String code;

    ServicePeriodType(int id, String code) {
        this.id = id;
        this.code = code;
    }

    public int getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    /**
     * 根据code获取对应的枚举值
     * @param code 代码字符串
     * @return 对应的枚举值，如果找不到返回null
     */
    public static ServicePeriodType fromCode(String code) {
        if (code == null) {
            return null;
        }
        for (ServicePeriodType type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        return null;
    }
}
