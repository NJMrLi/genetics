package com.genetics.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.EnumSet;

@JsonFormat(shape=JsonFormat.Shape.OBJECT)
public enum AuditStatus {
    //
    PASS(0,"通过"),
    REJECT(1,"驳回");

    AuditStatus(Integer id, String name) {
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

    public static EnumSet<AuditStatus> toEnumSet() {
        return EnumSet.allOf(AuditStatus.class);
    }

    public static String getName(int value) {

        for (AuditStatus serveState : AuditStatus.values()) {
            if (serveState.getId() == value) {
                return serveState.getName();
            }
        }

        return null;
    }
}
