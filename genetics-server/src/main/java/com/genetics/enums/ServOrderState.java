package com.genetics.enums;

/**
 * 订单状态枚举
 */
@Deprecated
public enum ServOrderState {

    WAIT_PAY(1, "待付款"),
    PAY_WAIT_CONFIRM(2, "付款待确认"),
    PAID(10, "已付款"),
    REFUNDING(20, "退款中"),
    PART_REFUND(21, "部分退款"),
    REFUNDED(22, "已退款"),
    COMPLETED(30, "已完成");

    private final Integer id;
    private final String name;

    ServOrderState(Integer id, String name) {
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
    public static ServOrderState getById(Integer id) {
        if (id == null) {
            return null;
        }
        for (ServOrderState state : values()) {
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
        ServOrderState state = getById(id);
        return state != null ? state.getName() : null;
    }

    /**
     * 检查ID是否存在
     */
    public static boolean contains(Integer id) {
        return getById(id) != null;
    }

}
