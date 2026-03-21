package com.genetics.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ServiceProviderType {

    AVASK(0, "AVASK"),
    EVATMASTER(1, "欧税通"),
    ITAXS(2, "跨税云"),
    MJCZ(3, "卖家成长"),
    KUANGXINTONG(4, "跨信通"),
    JP(5, "JP"),
    CAPTAIN_BI(6, "船长BI"),
    GENERAL_TESTING(7, "普惠检测"),
    STLB520(8, "三头六臂"),
    PUJIIP(9, "普集"),
    TAXUALLY(10, "泰税利"),
    TBVAT(11, "腾邦"),
    OTHER(99, "其他");

    @EnumValue
    private final int id;

    @EnumValue
    private final String displayName;

    /**
     * 根据id获取ServiceProviderType枚举
     * 
     * @param id 服务商类型ID
     * @return ServiceProviderType枚举，如果未找到返回null
     */
    public static ServiceProviderType getById(int id) {
        for (ServiceProviderType type : values()) {
            if (type.getId() == id) {
                return type;
            }
        }
        return null;
    }
}
