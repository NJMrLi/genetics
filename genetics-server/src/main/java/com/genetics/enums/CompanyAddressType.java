package com.genetics.enums;

import io.swagger.v3.oas.annotations.media.Schema;

public enum CompanyAddressType {

    @Schema(description = "注册地址")
    RegistAddress(1, "注册地址");

    private final Integer id;
    private final String name;

    CompanyAddressType(Integer id, String name) {
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
    public static CompanyAddressType fromTypeById(Integer id) {
        for (CompanyAddressType type : values()) {
            if (type.getId().equals(id)) {
                return type;
            }
        }
        throw new IllegalArgumentException("CompanyAddressType Entity Type id: " + id);
    }
}