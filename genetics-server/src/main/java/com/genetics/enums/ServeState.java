package com.genetics.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.EnumSet;

@JsonFormat(shape=JsonFormat.Shape.OBJECT)
public enum ServeState {
    //9:待服务 10:待提交，20 审核中 30 处理中 40 已完成 50 已驳回 99:已终止
    WAIT_SERVICE(9, "待服务", "info"),
    WAIT_SUBMIT(10, "待提交", "info"),
    AUITING(20, "待审核", "warning"),
    HANDING(30, "待递交", "warning"),
    ORGN_HANDING(31, "组织处理", "warning"),
    TAX_HANDING(32, "税局处理", "warning"),
    LOCAL_COLLEAGUE(33, "当地同事", "warning"),
    SUCCESS(40, "已完成", "success"),
    BACKED(50, "已驳回", "danger"),
    CANCLED(99, "已终止", "info");

    ServeState(Integer id, String name, String tagType) {
        this.id = id;
        this.name = name;
        this.tagType = tagType;
    }

    private Integer id;
    private String name;
    private String tagType;

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

    public String getTagType() {
        return tagType;
    }

    public void setTagType(String tagType) {
        this.tagType = tagType;
    }

    public static EnumSet<ServeState> toEprEnumSet() {

        EnumSet<ServeState> enumSet = EnumSet.noneOf(ServeState.class);
        enumSet.add(ServeState.WAIT_SERVICE);
        enumSet.add(ServeState.WAIT_SUBMIT);
        enumSet.add(ServeState.AUITING);
        enumSet.add(ServeState.HANDING);
        enumSet.add(ServeState.ORGN_HANDING);
        enumSet.add(ServeState.LOCAL_COLLEAGUE);
        enumSet.add(ServeState.SUCCESS);
        enumSet.add(ServeState.BACKED);
        enumSet.add(ServeState.CANCLED);
        return enumSet;
    }

    public static EnumSet<ServeState> toEnumSet() {
        return EnumSet.allOf(ServeState.class);
    }

    public static String getName(int value) {

        for (ServeState serveState : ServeState.values()) {
            if (serveState.getId() == value) {
                return serveState.getName();
            }
        }

        return null;
    }

    public static ServeState getServeStateById(int value) {
        for (ServeState serveState : ServeState.values()) {
            if (serveState.getId() == value) {
                return serveState;
            }
        }
        return null;
    }

}
