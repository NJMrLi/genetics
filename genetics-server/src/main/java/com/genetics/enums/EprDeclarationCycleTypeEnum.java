package com.genetics.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.util.EnumSet;


@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum EprDeclarationCycleTypeEnum {

    // 原先的1用不着，删除了，保留给后续新的申报周期类型。
    // 2 年度申报  3 半年度申报  4 季度申报  5 月度申报
    YEAR(2, "年度"),
    HALF_YEAR(3, "半年度"),
    QUARTER(4, "季度"),
    MONTH(5, "月度");

    private final Integer id;
    private final String name;

    EprDeclarationCycleTypeEnum(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public static EnumSet<EprDeclarationCycleTypeEnum> toEnumSet() {
        return EnumSet.allOf(EprDeclarationCycleTypeEnum.class);
    }

    public static String getTypeNameById(int value) {

        for (EprDeclarationCycleTypeEnum cycleTypeEnum : EprDeclarationCycleTypeEnum.values()) {
            if (cycleTypeEnum.getId() == value) {
                return cycleTypeEnum.getName();
            }
        }

        return null;
    }

    public static EprDeclarationCycleTypeEnum getTypeById(int value) {
        for (EprDeclarationCycleTypeEnum cycleTypeEnum :
                EprDeclarationCycleTypeEnum.values()) {
            if (cycleTypeEnum.getId() == value) {
                return cycleTypeEnum;
            }
        }
        return null;
    }

}
