package com.genetics.enums;

import io.swagger.v3.oas.annotations.media.Schema;

public enum CompanyContactType {

    @Schema(description = "微信")
    WECHAT(1, "WECHAT"),

    @Schema(description = "企业微信")
    WECOM(2, "WECOM"),

    @Schema(description = "QQ")
    QQ(3, "QQ"),

    @Schema(description = "钉钉")
    DINGDING(3, "DINGDING"),

    ;
    private final Integer id;
    private final String name;

    CompanyContactType(Integer id, String name) {
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
    public static CompanyContactType fromTypeById(Integer id) {
        for (CompanyContactType type : values()) {
            if (type.getId().equals(id)) {
                return type;
            }
        }
        throw new IllegalArgumentException("CompanyContactType Entity Type id: " + id);
    }
}
