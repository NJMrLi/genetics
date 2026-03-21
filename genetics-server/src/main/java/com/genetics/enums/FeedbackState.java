package com.genetics.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @author wangying
 */

@JsonFormat(shape=JsonFormat.Shape.OBJECT)
public enum FeedbackState {
    //1: 待处理 2:处理中，3 处理完成
    WAIT_HANDLE(1,"待处理"),
    HANDING(2,"处理中"),
    SUCCESS (3,"处理完成");

    FeedbackState(Integer id, String name) {
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
}
