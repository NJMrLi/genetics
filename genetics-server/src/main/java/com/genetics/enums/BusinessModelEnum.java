package com.genetics.enums;

/**
 * EPR经营方式枚举
 * @author Kris
 * @since 2025-10-24
 */
public enum BusinessModelEnum {

    /**
     * 远程销售商
     */
    DIS(1, "DIS", "远程销售", "Distance Seller"),

    /**
     * 制造商：在本国成立，制造并销售自有品牌的商品
     */
    FAB(2, "FAB", "制造商、在本国成立、制造并销售自有品牌的商品", "Manufacturer"),

    /**
     * 经销商：在本国成立，销售自有品牌或其他品牌的商品
     */
    REV(3, "REV", "经销商、在本国成立，销售自有品牌或其他品牌的商品", "Retailer"),

    /**
     * 进口商：在本国成立，并将来自其他国家的商品投放市场
     */
    IMP(4, "IMP", "进口商、在本国成立，并将来自其他国家的商品投放市场", "Importer"),

    /**
     * 介绍人
     */
    INT(5, "INT", "介绍人", "Introducer");

    /**
     * ID
     */
    private final Integer id;

    /**
     * 名称
     */
    private final String name;

    /**
     * 中文名称
     */
    private final String cnName;

    /**
     * 英文名称
     */
    private final String enName;

    BusinessModelEnum(Integer id, String name, String cnName, String enName) {
        this.id = id;
        this.name = name;
        this.cnName = cnName;
        this.enName = enName;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCnName() {
        return cnName;
    }

    public String getEnName() {
        return enName;
    }

    /**
     * 根据ID获取枚举
     * @param id ID
     * @return 枚举值
     */
    public static BusinessModelEnum getById(Integer id) {
        if (id == null) {
            return null;
        }
        for (BusinessModelEnum model : BusinessModelEnum.values()) {
            if (model.getId().equals(id)) {
                return model;
            }
        }
        return null;
    }

    /**
     * 根据名称获取枚举
     * @param name 名称
     * @return 枚举值
     */
    public static BusinessModelEnum getByName(String name) {
        if (name == null) {
            return null;
        }
        for (BusinessModelEnum model : BusinessModelEnum.values()) {
            if (model.getName().equals(name)) {
                return model;
            }
        }
        return null;
    }

    /**
     * 根据中文名称获取枚举
     * @param cnName 中文名称
     * @return 枚举值
     */
    public static BusinessModelEnum getByCnName(String cnName) {
        if (cnName == null) {
            return null;
        }
        for (BusinessModelEnum model : BusinessModelEnum.values()) {
            if (model.getCnName().equals(cnName)) {
                return model;
            }
        }
        return null;
    }

    /**
     * 根据英文名称获取枚举
     * @param enName 英文名称
     * @return 枚举值
     */
    public static BusinessModelEnum getByEnName(String enName) {
        if (enName == null) {
            return null;
        }
        for (BusinessModelEnum model : BusinessModelEnum.values()) {
            if (model.getEnName().equals(enName)) {
                return model;
            }
        }
        return null;
    }
}
