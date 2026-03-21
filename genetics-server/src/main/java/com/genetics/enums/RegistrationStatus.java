package com.genetics.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 注册号状态(10: 未生效 20: 生效中 30: 已失效)
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum RegistrationStatus {

    //注册号状态(10: 未生效 20: 生效中 30: 已失效)
    Inactive(10, "未生效"),
    Active(20, "生效中"),
    Expired(30, "已失效");

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

    RegistrationStatus(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

}
