package com.genetics.enums;

import lombok.Getter;

/**
 * 税号运营状态
 *
 * @author Kris
 * @date 2025/08/25
 */
@Getter
public enum VrnActiveStatus {

    /**
     * 正常
     */
    NORMAL(1,  "正常"),

    /**
     * 终止
     */
    TERMINATE(2, "终止");

    /**
     * 状态码
     */
    private final Integer code;


    /**
     * 状态描述
     */
    private final String description;

    VrnActiveStatus(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * 根据code获取枚举
     *
     * @param code 状态码
     * @return 对应的枚举，未找到返回null
     */
    public static VrnActiveStatus getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (VrnActiveStatus status : values()) {
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
        VrnActiveStatus status = getByCode(code);
        return status == null ? "" : status.getDescription();
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