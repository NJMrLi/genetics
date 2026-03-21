package com.genetics.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @author shuiGuobao
 * @create 2023-09-04 15:28
 */
@JsonFormat(shape=JsonFormat.Shape.OBJECT)
public enum ChannelDetailStatusEnum {
    // 待结算
    WAIT_SETTLEMENT(1,"待结算"),

    // 结算中
    SETTLEMENTING(2,"结算中"),

    // 已结算
    SETTLEMENTED(3,"已结算"),

    // 已剔除
    EXCLUDED(4,"已剔除");

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

    ChannelDetailStatusEnum(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public static String getChannelStatusNameById(Integer id){
        for(ChannelDetailStatusEnum channelStatusId : ChannelDetailStatusEnum.values()){
            if(channelStatusId.getId().equals(id)){
                return channelStatusId.getName();
            }
        }
        return null;
    }
}
