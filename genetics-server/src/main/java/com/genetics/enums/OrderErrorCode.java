package com.genetics.enums;

import com.genetics.common.BusinessException;

/**
 * 订单错误码枚举
 */
public enum OrderErrorCode {

    USER_OPENID_NOT_EXIST(4010004, "用户OpenId不存在"),
    COMPANY_NAME_REQUIRED(4010011, "公司名称不能为空"),

    // 注销相关错误消息
    DEREG_EXTS_FORMAT_ERROR(4010010, "无效的exts参数，注销商品exts格式为[['税号注册号', '期望注销日期yyyy-MM-dd', '注销原因', '当地仓库有库存(true|false)']]"),
    DEREG_EXTS_LENGTH_ERROR(4010010, "无效的exts参数，注销商品exts格式为['税号注册号', '期望注销日期yyyy-MM-dd', '注销原因', '当地仓库有无库存(true|false)']]"),

    // 续费相关错误消息
    RENEW_EXTS_FORMAT_ERROR(4010010, "无效的exts参数，续费商品exts不能为空"),
    RENEW_EXTS_LENGTH_ERROR(4010010, "无效的exts参数，续费商品exts参数长度不足"),

    // VAT补申报、改申报相关错误消息
    VAT_CHANGE_SUP_EXTS_FORMAT_ERROR(4010010, "无效的exts参数，VAT补/改申报商品exts不能为空"),
    VAT_CHANGE_SUP_EXTS_LENGTH_ERROR(4010010, "无效的exts参数，VAT补/改申报商品exts格式为['税号', '申报区间ID', '申报范围', '本土税号']"),
    VAT_CHANGE_SUP_TAX_NO_REQUIRED(4010010, "无效的exts参数，税号或本土税号至少需要提供一个"),
    VAT_CHANGE_SUP_CYCLE_REQUIRED(4010010, "无效的exts参数，申报区间ID不能为空"),
    VAT_CHANGE_SUP_RANGE_REQUIRED(4010010, "无效的exts参数，申报范围不能为空");

    private final int code;
    private final String message;

    OrderErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    /**
     * 根据code获取枚举
     */
    public static OrderErrorCode getByCode(int code) {
        for (OrderErrorCode errorCode : values()) {
            if (errorCode.getCode() == code) {
                return errorCode;
            }
        }
        return null;
    }

    /**
     * 根据code获取消息
     */
    public static String getMessageByCode(int code) {
        OrderErrorCode errorCode = getByCode(code);
        return errorCode != null ? errorCode.getMessage() : null;
    }

    /**
     * 检查code是否存在
     */
    public static boolean contains(int code) {
        return getByCode(code) != null;
    }

    /**
     * 抛出异常
     */
    public void throwException() {
        throw new BusinessException(this.code, this.message);
    }

    /**
     * 抛出异常（带格式化消息）
     */
    public void throwException(Object... args) {
        String formattedMessage = String.format(this.message, args);
        throw new BusinessException(this.code, formattedMessage);
    }
}
