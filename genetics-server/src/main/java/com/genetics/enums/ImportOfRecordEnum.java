package com.genetics.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 进口记录类型枚举
 */
@Getter
@AllArgsConstructor
public enum ImportOfRecordEnum {

    BUYER("BUYER", "买方"),
    SELLER("SELLER", "卖方");

    private final String code;
    private final String name;
}
