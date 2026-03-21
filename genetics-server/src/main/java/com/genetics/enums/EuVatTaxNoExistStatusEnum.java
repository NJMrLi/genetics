package com.genetics.enums;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 欧盟VAT税号状态枚举
 * 仅用于西班牙-转代理场景
 *
 * @author cheng
 * @since 2025-01-15
 */
@Schema(description = "欧盟VAT税号状态枚举")
public enum EuVatTaxNoExistStatusEnum {

    /**
     * 已有欧盟VAT税号
     */
    HAS_EU_VAT(1, "已有欧盟VAT税号"),

    /**
     * 无欧盟VAT税号，且需申请
     */
    NO_EU_VAT_NEED_APPLY(2, "无欧盟VAT税号，且需申请"),

    /**
     * 无欧盟VAT税号，且不需要申请
     */
    NO_EU_VAT_NO_NEED_APPLY(3, "无欧盟VAT税号，且不需要申请");

    private final Integer code;
    private final String description;

    EuVatTaxNoExistStatusEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public Integer getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    /**
     * 根据code获取枚举
     *
     * @param code 编码
     * @return 枚举值
     */
    public static EuVatTaxNoExistStatusEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (EuVatTaxNoExistStatusEnum value : values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

    /**
     * 根据code获取描述
     *
     * @param code 编码
     * @return 描述
     */
    public static String getDescriptionByCode(Integer code) {
        EuVatTaxNoExistStatusEnum status = getByCode(code);
        return status != null ? status.getDescription() : null;
    }

    /**
     * 检查是否为有效的状态
     *
     * @param code 编码
     * @return 是否有效
     */
    public static boolean isValid(Integer code) {
        return getByCode(code) != null;
    }
}

