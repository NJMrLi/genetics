package com.genetics.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.EnumSet;

@JsonFormat(shape=JsonFormat.Shape.OBJECT)
public enum ServeSupplyInfoState {
    //服务状态id，1: 草稿 10:待提交 20 审核中 30 已完成 40 已驳回
    //DRAFT(1,"草稿"),
    WAIT_SUBMIT(10,"待提交"),
    AUITING(20,"待审核"),
    SUCCESS (30,"已完成"),
    BACKED(40,"已驳回");

    ServeSupplyInfoState(Integer id, String name) {
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


    public static EnumSet<ServeSupplyInfoState> toEnumSet() {
        return EnumSet.allOf(ServeSupplyInfoState.class);
    }


    public static String getName(int value) {
        for (ServeSupplyInfoState serveState : ServeSupplyInfoState.values()) {
            if (serveState.getId() == value) {
                return serveState.getName();
            }
        }

        return null;
    }
}
