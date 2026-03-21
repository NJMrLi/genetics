package com.genetics.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @author cy
 * @date 2022/11/8 9:43
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ApplySepaStatusEnum {

    NO_APPLY(0, "不申请"),
    ALREADY_APPLY(1, "申请"),
    HAVE_APPLY(2, "已经拥有");

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

    ApplySepaStatusEnum(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

}
