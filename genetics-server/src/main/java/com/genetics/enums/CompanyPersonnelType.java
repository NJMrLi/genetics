package com.genetics.enums;

import io.swagger.v3.oas.annotations.media.Schema;

public enum CompanyPersonnelType {
    // a、个人
    @Schema(description = "法人")
    LegalPerson(1, "法人"),

    // b、企业
    @Schema(description = "联系人")
    ContactPerson(2, "联系人");

    private final Integer id;
    private final String name;

    CompanyPersonnelType(Integer id, String name) {
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
    public static CompanyPersonnelType fromTypeById(Integer id) {
        for (CompanyPersonnelType type : values()) {
            if (type.getId().equals(id)) {
                return type;
            }
        }
        throw new IllegalArgumentException("CompanyPersonnelType Entity Type id: " + id);
    }
}