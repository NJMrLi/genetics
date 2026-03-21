package com.genetics.enums;

/**
 * 订单来源枚举
 */
@Deprecated
public enum ServOrderSource {

    DIRECT(1, "直客"),
    VISA(2, "VISA"),
    ACES(3, "ACES");

    private final Integer id;
    private final String name;

    ServOrderSource(Integer id, String name) {
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
     * 根据ID获取枚举
     */
    public static ServOrderSource getById(Integer id) {
        if (id == null) {
            return null;
        }
        for (ServOrderSource source : values()) {
            if (source.getId().equals(id)) {
                return source;
            }
        }
        return null;
    }

    /**
     * 根据ID获取名称
     */
    public static String getNameById(Integer id) {
        ServOrderSource source = getById(id);
        return source != null ? source.getName() : null;
    }

    /**
     * 检查ID是否存在
     */
    public static boolean contains(Integer id) {
        return getById(id) != null;
    }

}
