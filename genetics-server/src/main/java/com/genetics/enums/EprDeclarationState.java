package com.genetics.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.EnumSet;

@JsonFormat(shape=JsonFormat.Shape.OBJECT)
public enum EprDeclarationState {
    WAIT_SUBMIT(50,"待提交"),
    CANCLED(60,"已终止");

    EprDeclarationState(Integer id, String name) {
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


    public static EnumSet<EprDeclarationState> toEprEnumSet() {

        EnumSet enumSet = EnumSet.noneOf(EprDeclarationState.class);
        enumSet.add(EprDeclarationState.WAIT_SUBMIT);
        enumSet.add(EprDeclarationState.CANCLED);
        return enumSet;
    }

    public static EnumSet<EprDeclarationState> toEnumSet() {
        return EnumSet.allOf(EprDeclarationState.class);
    }

    public static String getName(int value) {

        for (EprDeclarationState serveState : EprDeclarationState.values()) {
            if (serveState.getId() == value) {
                return serveState.getName();
            }
        }

        return null;
    }
}
