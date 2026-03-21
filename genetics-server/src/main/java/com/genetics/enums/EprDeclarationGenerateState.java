package com.genetics.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.EnumSet;

@JsonFormat(shape=JsonFormat.Shape.OBJECT)
public enum EprDeclarationGenerateState {
    NOT_GENERATED (0,"未生成"),
    GENERATED(1,"以生成");

    EprDeclarationGenerateState(Integer id, String name) {
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


    public static EnumSet<EprDeclarationGenerateState> toEnumSet() {
        return EnumSet.allOf(EprDeclarationGenerateState.class);
    }

    public static String getName(int value) {

        for (EprDeclarationGenerateState serveState : EprDeclarationGenerateState.values()) {
            if (serveState.getId() == value) {
                return serveState.getName();
            }
        }

        return null;
    }
}
