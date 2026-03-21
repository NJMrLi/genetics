package com.genetics.enums;

import io.swagger.v3.oas.annotations.media.Schema;

public enum CompanyPersonnelCertificateType {
    // a、身份证
    @Schema(description = "身份证")
    IDCARD(10132, "身份证"),

    // b、护照
    @Schema(description = "护照")
    PASSPORT(10133, "护照");

    private final Integer id;
    private final String name;

    CompanyPersonnelCertificateType(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    /**
     * 根据代码获取对应的枚举值
     *
     * @param id 枚举代码
     * @return 对应的枚举值，如果找不到则抛出异常
     */
    public static CompanyPersonnelCertificateType fromTypeById(Integer id) {
        for (CompanyPersonnelCertificateType type : values()) {
            if (type.getId().equals(id)) {
                return type;
            }
        }
        throw new IllegalArgumentException("CompanyPersonnelType Entity Type id: " + id);
    }
}
