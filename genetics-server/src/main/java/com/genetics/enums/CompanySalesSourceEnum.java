package com.genetics.enums;

import lombok.Getter;

/**
 * 公司销售店铺数据来源枚举
 */
@Getter
public enum CompanySalesSourceEnum {

    /**
     * 服务单
     */
    SERVE_LIST(0, "服务单"),

    /**
     * 手动创建
     */
    MANUAL_CREATE(1, "手动创建");

    private final Integer code;
    private final String value;

    CompanySalesSourceEnum(Integer code, String value) {
        this.code = code;
        this.value = value;
    }
}
