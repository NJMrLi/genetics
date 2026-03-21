package com.genetics.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @author cy
 * @date 2022/11/8 9:43
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum PaymentMethod {
    ALI(1, "支付宝"),
    WEI_XIN(2, "微信"),
    OFF_LINE(3, "线下"),
    PICK_UP(4, "皮卡"),
    BALANCE(5, "余额支付");

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

    PaymentMethod(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public static PaymentMethod getPaymentMethodName(Integer id) {
        for (PaymentMethod pm : PaymentMethod.values()) {
            if (pm.getId() != null && pm.getId().intValue() == id.intValue()) {
                return pm;
            }
        }
        return null;
    }

    public static String getName(int value) {

        for (PaymentMethod paymentMethod : PaymentMethod.values()) {
            if (paymentMethod.getId() == value) {
                return paymentMethod.getName();
            }
        }

        return null;
    }
}
