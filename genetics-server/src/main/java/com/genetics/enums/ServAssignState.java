package com.genetics.enums;

/**
 * 订单分配状态
 */
public enum ServAssignState {
    ALL(0, "全部"),
    NOT_ASSIGNED(1, "未分配"),
    ASSIGNED(2, "已分配"),
    COMPLETED(3, "完成"),
    CLOSED(4, "关闭");

    private final Integer id;
    private final String name;

    ServAssignState(Integer id, String name) {
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
    public static ServAssignState getById(Integer id) {
        if (id == null) {
            return null;
        }
        for (ServAssignState state : values()) {
            if (state.getId().equals(id)) {
                return state;
            }
        }
        return null;
    }

    /**
     * 根据ID获取名称
     */
    public static String getNameById(Integer id) {
        ServAssignState state = getById(id);
        return state != null ? state.getName() : null;
    }

    /**
     * 检查ID是否存在
     */
    public static boolean contains(Integer id) {
        return getById(id) != null;
    }

}
