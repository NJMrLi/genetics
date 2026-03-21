package com.genetics.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

/**
 * 申报数据列类型枚举
 */
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum DeclarationColumnTypeEnum {

    CATEGORY("CATEGORY", "分类"),
    BRAND_NAME("BRAND_NAME", "品牌名称"),

    SINGLE_WEIGHT("SINGLE_WEIGHT", "单个重量"),
    QUANTITY("QUANTITY", "数量"),
    WEIGHT("WEIGHT", "重量"),

    CURRENT_DECLARATION_AMAZON("CURRENT_DECLARATION_AMAZON", "本次申报（Amazon）"),
    CURRENT_DECLARATION_OTHER("CURRENT_DECLARATION_OTHER", "本次申报（Other）"),
    CURRENT_DECLARATION("CURRENT_DECLARATION", "本次申报"),

    CURRENT_DECLARATION_EXCESS("CURRENT_DECLARATION_EXCESS", "本次申报超额"),
    PRE_DECLARATION("PRE_DECLARATION", "预申报"),
    CUMULATIVE_DECLARATION("CUMULATIVE_DECLARATION", "累计已申报"),

    BASE_DECLARATION("BASE_DECLARATION", "基础申报"),

    ;

    private final String code;
    private final String name;

    DeclarationColumnTypeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static DeclarationColumnTypeEnum valueOfCode(String code) {
        for (DeclarationColumnTypeEnum item : DeclarationColumnTypeEnum.values()) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }
}

