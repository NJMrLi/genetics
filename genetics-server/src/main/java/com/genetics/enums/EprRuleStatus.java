package com.genetics.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.EnumSet;

@JsonFormat(shape=JsonFormat.Shape.OBJECT)
public enum EprRuleStatus {
    //
    DISABLE(0,"禁用"),
    ENABLE(1,"启用");

    EprRuleStatus(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    private Integer id;
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
