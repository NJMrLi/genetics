package com.genetics.enums;

import com.genetics.common.BusinessException;

public enum CompanyBusinessLicenseType {
    BUSINESS_LICENSE(1, "营业执照"),
    OVERSEAS_REGISTRATION(2, "境外注册登记证");

    private final int id;
    private final String name;

    CompanyBusinessLicenseType(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    /**
     * 根据 id 获取枚举
     */
    public static CompanyBusinessLicenseType ofId(int id) {
        for (CompanyBusinessLicenseType e : values()) {
            if (e.id == id) return e;
        }
        throw new BusinessException(-1, "营业执照类型不存在");
    }
}
