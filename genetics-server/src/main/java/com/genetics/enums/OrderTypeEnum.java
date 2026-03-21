package com.genetics.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @author shuiGuobao
 * @create 2023-09-06 13:32
 */
@JsonFormat(shape=JsonFormat.Shape.OBJECT)
public enum OrderTypeEnum {
    // 订单类型id：1代理 2渠道
    AGENT(1,"代理"),
    CHANNEL(2,"渠道"),;

    private Integer id;
    private String name;

    OrderTypeEnum(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

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

    // 根据id获取订单类型名称
    public static String getOrderTypeNameById(Integer id){
        for(OrderTypeEnum orderTypeId : OrderTypeEnum.values()){
            if(orderTypeId.getId().equals(id)){
                return orderTypeId.getName();
            }
        }
        return null;
    }
}
