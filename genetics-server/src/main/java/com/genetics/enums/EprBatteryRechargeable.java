package com.genetics.enums;

import java.util.EnumSet;

public enum EprBatteryRechargeable {

    NOT_SUPPORT("0", "不支持"),
    SUPPORT("1", "支持");

    EprBatteryRechargeable(String code, String name) {
        this.code = code;
        this.name = name;
    }

    private String code;
    private String name;

    public String getCode() {
        return code;
    }

    public void setId(Integer id) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static EnumSet<EprRuleStatus> toEnumSet() {
        return EnumSet.allOf(EprRuleStatus.class);
    }

    public static String getName(int value) {

        for (EprRuleStatus serveState : EprRuleStatus.values()) {
            if (serveState.getId() == value) {
                return serveState.getName();
            }
        }

        return null;
    }
}
