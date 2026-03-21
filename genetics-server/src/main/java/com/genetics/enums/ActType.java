package com.genetics.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.EnumSet;

/**
 * 法案类型
 */
@JsonFormat(shape=JsonFormat.Shape.OBJECT)
public enum ActType {
    
    PKG(10,"PKG", "包装法"),
    WEEE(20,"WEEE", "WEEE"),
    BAT(30,"BAT", "电池法")
    ;

    ActType(Integer id, String name, String desc) {
        this.id = id;
        this.name = name;
        this.desc = desc;
    }

    private Integer id;
    private String name;
    private String desc;

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


    public static EnumSet<ActType> toEnumSet() {
        return EnumSet.allOf(ActType.class);
    }


    public static String getName(int value) {

        for (ActType serveState : ActType.values()) {
            if (serveState.getId() == value) {
                return serveState.getName();
            }
        }

        return null;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
