package com.genetics.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public enum UKDeclarationCycleType {

    QUARTER_14710(1, "非自然季度: 1,4,7,10", Arrays.asList(1, 4, 7, 10)),
    QUARTER_25811(2, "非自然季度: 2,5,8,11", Arrays.asList(2, 5, 8, 11)),
    QUARTER_36912(3, "非自然季度: 3,6,9,12", Arrays.asList(3, 6, 9, 12));

    private final Integer id;
    private final String name;
    /**
     * 申报结束月份列表
     */
    private final List<Integer> endMonths;

    UKDeclarationCycleType(int id, String name, List<Integer> endMonths) {
        this.id = id;
        this.name = name;
        this.endMonths = endMonths;
    }

    public static UKDeclarationCycleType getTypeById(Integer id) {
        for (UKDeclarationCycleType t : values()) {
            if (t.getId().equals(id)) {
                return t;
            }
        }
        return null;
    }

    /**
     * 根据申报结束月份获取对应的申报周期类型
     *
     * @param endMonth 申报结束月份（1-12）
     * @return 对应的申报周期类型，如果找不到则返回null
     */
    public static UKDeclarationCycleType getByEndMonth(int endMonth) {
        for (UKDeclarationCycleType t : values()) {
            if (t.getEndMonths().contains(endMonth)) {
                return t;
            }
        }
        throw new RuntimeException("非法的月份：" + endMonth);
    }

}
