package com.genetics.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * 公司销售店铺授权状态枚举
 */
@Getter
public enum CompanySalesAuthStatusEnum {

    /**
     * 未授权
     */
    UNAUTHORIZED(0, "未授权"),

    /**
     * 已授权
     */
    AUTHORIZED(1, "已授权"),

    /**
     * 已失效
     */
    EXPIRED(2, "已失效");

    @EnumValue
    private final Integer code;
    private final String value;

    CompanySalesAuthStatusEnum(Integer code, String value) {
        this.code = code;
        this.value = value;
    }
}
