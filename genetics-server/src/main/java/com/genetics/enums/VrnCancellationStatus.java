package com.genetics.enums;

import lombok.Getter;

/**
 * 税号注销状态枚举
 *
 * @author Kris
 * @date 2025/08/25
 */
@Getter
public enum VrnCancellationStatus {

    /**
     * 生效中
     */
    IN_EFFECT(1, "否", "生效中"),

    /**
     * 已注销
     */
    CANCELLED(2, "是", "已注销");

    /**
     * 状态码
     */
    private final Integer code;

    /**
     * 是否注销（简写）
     */
    private final String cancelled;

    /**
     * 状态描述
     */
    private final String description;

    VrnCancellationStatus(Integer code, String cancelled, String description) {
        this.code = code;
        this.cancelled = cancelled;
        this.description = description;
    }

    /**
     * 根据code获取枚举
     *
     * @param code 状态码
     * @return 对应的枚举，未找到返回null
     */
    public static VrnCancellationStatus getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (VrnCancellationStatus status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }

    /**
     * 根据code获取描述
     *
     * @param code 状态码
     * @return 状态描述，未找到返回空字符串
     */
    public static String getDescriptionByCode(Integer code) {
        VrnCancellationStatus status = getByCode(code);
        return status == null ? "" : status.getDescription();
    }

    /**
     * 根据code获取是否注销（简写）
     *
     * @param code 状态码
     * @return "是"或"否"，未找到返回空字符串
     */
    public static String getCancelledByCode(Integer code) {
        VrnCancellationStatus status = getByCode(code);
        return status == null ? "" : status.getCancelled();
    }

    /**
     * 判断code是否有效
     *
     * @param code 状态码
     * @return 是否有效
     */
    public static boolean isValid(Integer code) {
        return getByCode(code) != null;
    }
}