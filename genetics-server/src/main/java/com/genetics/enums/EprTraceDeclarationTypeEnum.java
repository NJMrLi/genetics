package com.genetics.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.EnumSet;

/**
 * @author cy
 * @date 2022/11/8 9:43
 */
@JsonFormat(shape=JsonFormat.Shape.OBJECT)
public enum EprTraceDeclarationTypeEnum {

    NONE(0,"无"),
    BEFORE(1,"下号前"),
    AFTER(2,"下号后");

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

    EprTraceDeclarationTypeEnum(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public static EnumSet<EprTraceDeclarationTypeEnum> toEnumSet() {
        return EnumSet.allOf(EprTraceDeclarationTypeEnum.class);
    }

    public static String getNameTypeId(int value) {

        for (EprTraceDeclarationTypeEnum cycleTypeEnum : EprTraceDeclarationTypeEnum.values()) {
            if (cycleTypeEnum.getId() == value) {
                return cycleTypeEnum.getName();
            }
        }

        return null;
    }

}
