package com.genetics.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author cy
 * @date 2022/11/8 9:43
 */
@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum TaxNoType {

    // 1.VAT税号 2.VAT税务识别号 3.epr注册号 4 注册国税号 5 OSS 6 IOSS
    VAT_TAX_NO(1, "VAT税号"),
    VAT_DISCERN_TAX_NO(2, "VAT税务识别号"),
    // 在原实现中，3又代表包装法，改为统一使用枚举值。在后续开发过程中了解区别
    EPR_REG_NO(3, "EPR注册号"),
    REG_COUNTRY_TAX_NO(4, "注册国税号"),
    OSS_REG_NO(5, "OSS号"),
    IOSS_REG_NO(6, "IOSS号"),
    JCT_NO(7, "JCT号"),
    GST_NO(8, "GST号");

    @EnumValue
    private final Integer id;
    private final String name;

    public static boolean isVatTexNo(int id) {

        if (id == VAT_TAX_NO.getId() ||
                id == VAT_DISCERN_TAX_NO.getId() ||
                id == OSS_REG_NO.getId() ||
                id == IOSS_REG_NO.getId() ||
                id == JCT_NO.getId()) {
            return true;
        }
        return false;
    }

    public static boolean isEPRTexNo(int id) {
        if (id == EPR_REG_NO.getId() ||
                id == REG_COUNTRY_TAX_NO.getId()) {
            return true;
        }
        return false;
    }


}
