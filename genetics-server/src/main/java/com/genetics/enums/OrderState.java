package com.genetics.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.EnumSet;

/**
 * @author cy
 * @date 2022/11/10 16:25
 */
@JsonFormat(shape=JsonFormat.Shape.OBJECT)
public enum OrderState {
    //订单状态，1:待付款 2:付款待确认 10:已付款
    // 20:退款中 21:部分退款 22:已退款，23:退款失败，30:已完成，40:已关闭，50:支付失败
    PAY(1,"待付款"),
    PAY_CONFIRM(2,"付款待确认"),
    PAID(10,"已付款"),
    REFUNDING(20,"退款中"),
    PARTIAL_REFUND(21,"部分退款"),
    REFUNDED(22,"已退款"),
    REFUNDED_FAIL(23,"退款失败"),
    COMPLETED(30,"已完成"),
    CLOSED(40,"已关闭"),
    PAY_FAIL(50,"支付失败")
    ;

    OrderState(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    private Integer id;
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public static EnumSet<OrderState> toEnumSet() {
        return EnumSet.allOf(OrderState.class);
    }


}
