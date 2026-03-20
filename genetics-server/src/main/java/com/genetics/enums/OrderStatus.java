package com.genetics.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

/**
 * 服务单业务状态枚举
 */
@Getter
public enum OrderStatus {
    PENDING_SUBMIT(10,  "待提交",     "info"),
    PENDING_REVIEW(20,  "待审核",     "warning"),
    PENDING_SUBMIT_LOCAL(30, "待递交", "warning"),
    ORG_PROCESSING(31,  "组织处理",   "primary"),
    TAX_PROCESSING(32,  "税局处理",   "primary"),
    LOCAL_PROCESSING(33,"当地同事处理","primary"),
    COMPLETED(40,       "已完成",     "success"),
    REJECTED(50,        "已驳回",     "danger"),
    TERMINATED(99,      "已终止",     "danger");

    private final int    code;
    private final String name;
    /** 前端 tag 类型 */
    private final String tagType;

    OrderStatus(int code, String name, String tagType) {
        this.code    = code;
        this.name    = name;
        this.tagType = tagType;
    }

    public static Optional<OrderStatus> of(int code) {
        return Arrays.stream(values()).filter(s -> s.code == code).findFirst();
    }

    public static String nameOf(int code) {
        return of(code).map(OrderStatus::getName).orElse("未知状态");
    }
}
