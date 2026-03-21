package com.genetics.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 在线缴费状态枚举
 * 
 * @author 系统生成
 * @date 2024/01/01
 */
@Getter
@AllArgsConstructor
public enum OnlinePaymentStatusEnum {

    /**
     * 待缴费
     */
    PENDING_PAYMENT(0, "待缴费"),

    /**
     * 驳回
     */
    REJECTED(1, "缴费驳回"),

    /**
     * 缴费待确认
     */
    PAYMENT_PENDING_CONFIRM(2, "缴费待确认"),

    /**
     * 已缴费
     */
    PAID(3, "已缴费"),

    /**
     * 取消缴费
     */
    CANCELLED(4, "取消缴费");

    private final Integer code;
    private final String name;

    /**
     * 根据代码获取枚举
     * 
     * @param code 代码
     * @return 枚举值
     */
    public static OnlinePaymentStatusEnum getByCode(Integer code) {
        for (OnlinePaymentStatusEnum status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }

    /**
     * 根据代码获取名称
     * 
     * @param code 代码
     * @return 名称
     */
    public static String getNameByCode(Integer code) {
        OnlinePaymentStatusEnum status = getByCode(code);
        return status != null ? status.getName() : null;
    }
}
