package com.genetics.enums;

import lombok.Getter;

/**
 * 服务单实例状态枚举
 */
@Getter
public enum InstanceStatus {
    DRAFT(0, "草稿"),
    SUBMITTED(1, "已提交"),
    AUDITED(2, "已审核");

    private final int code;
    private final String desc;

    InstanceStatus(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
