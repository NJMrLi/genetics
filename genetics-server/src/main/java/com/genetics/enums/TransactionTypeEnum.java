package com.genetics.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @author shuiGuobao
 * @create 2023-09-06 14:10
 */
@JsonFormat(shape=JsonFormat.Shape.OBJECT)
public enum TransactionTypeEnum {
    // 交易类型id  1：支付 2：退款
    PAY(1,"支付"),
    REFUND(2,"退款");

    private Integer id;
    private String name;

    TransactionTypeEnum(Integer id, String name) {
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

    // 根据id获取交易类型名称
    public static String getTransactionTypeNameById(Integer id){
        for(TransactionTypeEnum transactionTypeId : TransactionTypeEnum.values()){
            if(transactionTypeId.getId().equals(id)){
                return transactionTypeId.getName();
            }
        }
        return null;
    }
}
