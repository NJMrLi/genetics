package com.genetics.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.genetics.common.BusinessException;
import lombok.Getter;

import java.time.LocalDate;

/**
 * @author shuiGuobao
 * @create 2023-11-07 10:22
 */
@Getter
public enum CompanyTaxNoStatusEnum {
    // 1无服务 10 服务未开始 20 服务中 30 服务过期 40 已终止
    NO_SERVICE(1, "无服务"),
    NOT_STARTED(10, "服务未开始"),
    EFFECTIVE(20, "服务中"),
    INVALID(30, "服务过期"),
    CANCELLED(40, "已终止");

    @EnumValue
    private final Integer id;
    private final String name;

    CompanyTaxNoStatusEnum(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * 获取服务状态
     *
     * @param startTime
     * @param endTime
     * @param vrnActiveStatus
     * @return
     */
    public static CompanyTaxNoStatusEnum checkAndGetCompanyTaxNoServiceStatus(LocalDate startTime, LocalDate endTime, VrnActiveStatus vrnActiveStatus) {
        LocalDate date = LocalDate.now();
        //设置服务期间
        if (null != startTime && null != endTime) {

            if (endTime.isBefore(startTime)) {
                throw new BusinessException(-1, "服务结束时间不能在开始时间之前");
            }

            if (date.isAfter(endTime)) {
                //服务过期并且，状态是已终止
                if (vrnActiveStatus.equals(VrnActiveStatus.TERMINATE)) {
                    return CompanyTaxNoStatusEnum.CANCELLED;
                }
                //单纯的服务过期
                return CompanyTaxNoStatusEnum.INVALID;
            } else if (date.isBefore(startTime)) {
                //服务还没开始
                return CompanyTaxNoStatusEnum.NOT_STARTED;
            } else {
                // 包含开始日期、结束日期和中间的所有日期
                return CompanyTaxNoStatusEnum.EFFECTIVE;
            }
        }
        return CompanyTaxNoStatusEnum.NO_SERVICE;
    }

}
