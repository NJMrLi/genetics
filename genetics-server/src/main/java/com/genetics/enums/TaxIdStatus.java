package com.genetics.enums;

/**
 * 税号状态枚举
 */
public enum TaxIdStatus {

    /**
     * 全部待下号
     */
    ALL_PENDING(1, "全部待下号"),

    /**
     * 本土税号待下号
     */
    LOCAL_TAX_ID_PENDING(2, "本土税号待下号"),

    /**
     * 税号待下号
     */
    TAX_ID_PENDING(3, "税号待下号"),

    /**
     * EORI待下号
     */
    EORI_PENDING(4, "EORI待下号"),

    /**
     * 全部已下号
     */
    ALL_OK(5, "全部已下号");

    /**
     * 状态码
     */
    private final Integer id;

    /**
     * 状态描述
     */
    private final String description;

    /**
     * 构造函数
     *
     * @param id          状态码
     * @param description 状态描述
     */
    TaxIdStatus(Integer id, String description) {
        this.id = id;
        this.description = description;
    }

    /**
     * 获取枚举值对应的状态码
     *
     * @return 状态码
     */
    public Integer getId() {
        return id;
    }

    /**
     * 获取枚举值对应的状态描述
     *
     * @return 状态描述
     */
    public String getDescription() {
        return description;
    }

    /**
     * 通过状态码获取对应的枚举常量
     *
     * @param id 状态码
     * @return 对应的枚举常量，如果未找到则返回null
     */
    public static TaxIdStatus getById(Integer id) {
        for (TaxIdStatus status : values()) {
            if (status.getId().equals(id)) {
                return status;
            }
        }
        return null;
    }

    /**
     * 通过状态描述获取对应的枚举常量
     *
     * @param description 状态描述
     * @return 对应的枚举常量，如果未找到则返回null
     */
    public static TaxIdStatus getByDescription(String description) {
        for (TaxIdStatus status : values()) {
            if (status.getDescription().equals(description)) {
                return status;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return this.id + ": " + this.description;
    }
}
