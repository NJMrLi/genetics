package com.genetics.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.EnumSet;

/**
 * 法案类型
 */
@JsonFormat(shape=JsonFormat.Shape.OBJECT)
public enum BellaCompanyEnum {

    LOB(10192,"个体工商户", "0"),
    LBC(10131,"股份有限公司", "4"),
    LLC(10130,"有限责任公司", "3"),
    PS(10129,"合伙企业", "5"),
    ST(10128,"独资企业", "1");

    BellaCompanyEnum(Integer id, String name, String item) {
        this.id = id;
        this.name = name;
        this.item = item;
    }

    private Integer id;

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

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    private String name;
    private String item;

    public static EnumSet<BellaCompanyEnum> toEnumSet() {
        return EnumSet.allOf(BellaCompanyEnum.class);
    }

    public static String getItem(int value) {

        for (BellaCompanyEnum serveState : BellaCompanyEnum.values()) {
            if (serveState.getId() == value) {
                return serveState.getItem();
            }
        }

        return null;
    }

    public static String getName(int value) {

        for (BellaCompanyEnum serveState : BellaCompanyEnum.values()) {
            if (serveState.getId() == value) {
                return serveState.getName();
            }
        }

        return null;
    }

}
