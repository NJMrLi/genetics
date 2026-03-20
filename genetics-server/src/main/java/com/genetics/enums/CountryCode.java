package com.genetics.enums;

import lombok.Getter;

/**
 * 支持的国家代码枚举
 */
@Getter
public enum CountryCode {
    DEU("DEU", "德国", "Germany"),
    FRA("FRA", "法国", "France"),
    ITA("ITA", "意大利", "Italy"),
    ESP("ESP", "西班牙", "Spain"),
    POL("POL", "波兰", "Poland"),
    CZE("CZE", "捷克", "Czech Republic"),
    GBR("GBR", "英国", "United Kingdom");

    private final String code;
    private final String nameCn;
    private final String nameEn;

    CountryCode(String code, String nameCn, String nameEn) {
        this.code = code;
        this.nameCn = nameCn;
        this.nameEn = nameEn;
    }

    public static boolean isValid(String code) {
        for (CountryCode c : values()) {
            if (c.code.equals(code)) return true;
        }
        return false;
    }
}
