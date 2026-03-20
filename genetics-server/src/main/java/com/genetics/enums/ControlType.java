package com.genetics.enums;

import lombok.Getter;

/**
 * 控件类型枚举
 */
@Getter
public enum ControlType {
    INPUT("INPUT", "输入框"),
    TEXTAREA("TEXTAREA", "多行文本"),
    NUMBER("NUMBER", "数字输入"),
    SELECT("SELECT", "下拉框"),
    SWITCH("SWITCH", "开关"),
    UPLOAD("UPLOAD", "文件上传"),
    DATE("DATE", "日期选择");

    private final String code;
    private final String desc;

    ControlType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
