package com.genetics.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum  EprTemplateColumnName {

    // ===== 费用列 (Fee Columns) =====
    PRODUCT_PRICE("商品单价", "FEE"),

    //公司的成本价格
    COMPANY_PRICE("成本单价", "FEE"),

    // ===== 指标列 (Indicator Columns) =====
    QUANTITY("数量", "INDICATOR"),
    WEIGHT("重量", "INDICATOR"),
    SINGLE_WEIGHT("单个重量", "INDICATOR");

    /**
     * 列显示名
     */
    private final String name;

    /**
     * 列类型: FEE 或 INDICATOR
     */
    private final String type;
}
