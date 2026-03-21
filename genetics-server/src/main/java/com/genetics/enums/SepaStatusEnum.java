package com.genetics.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @author cy
 * @date 2022/11/8 9:43
 */
@JsonFormat(shape=JsonFormat.Shape.OBJECT)
public enum SepaStatusEnum {

    NOT_HAVE(1,"没有SPEA"),
    ALREADY_HAVE(2,"拥有SEPA");

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

    SepaStatusEnum(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

}
