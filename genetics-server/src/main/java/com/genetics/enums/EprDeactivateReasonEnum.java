package com.genetics.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * EPR注销原因枚举
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum EprDeactivateReasonEnum {

    COMPANY_DEREGISTERED(1, "公司注销，不再继续经营", "The company has been deregistered and no longer in operation"),
    NO_LONGER_SELL_PRODUCTS(2, "不再往该国销售对应产品", "No longer sell corresponding products in current country"),
    NO_LONGER_CROSS_BORDER_SALES(3, "不再往该国跨境销售", "No longer cross-border sales to current country"),
    OTHER(4, "其他", "Other");

    private final Integer id;
    private final String cnName;
    private final String enName;

    EprDeactivateReasonEnum(Integer id, String cnName, String enName) {
        this.id = id;
        this.cnName = cnName;
        this.enName = enName;
    }

    public Integer getId() {
        return id;
    }

    public String getCnName() {
        return cnName;
    }

    public String getEnName() {
        return enName;
    }

    /**
     * 根据ID获取枚举
     *
     * @param id ID
     * @return 枚举值
     */
    public static EprDeactivateReasonEnum getById(Integer id) {
        if (id == null) {
            return null;
        }
        for (EprDeactivateReasonEnum reason : EprDeactivateReasonEnum.values()) {
            if (reason.getId().equals(id)) {
                return reason;
            }
        }
        return null;
    }

    /**
     * 根据中文名称获取枚举
     *
     * @param cnName 中文名称
     * @return 枚举值
     */
    public static EprDeactivateReasonEnum getByCnName(String cnName) {
        if (cnName == null) {
            return null;
        }
        for (EprDeactivateReasonEnum reason : EprDeactivateReasonEnum.values()) {
            if (reason.getCnName().equals(cnName)) {
                return reason;
            }
        }
        return null;
    }

    /**
     * 根据英文名称获取枚举
     *
     * @param enName 英文名称
     * @return 枚举值
     */
    public static EprDeactivateReasonEnum getByEnName(String enName) {
        if (enName == null) {
            return null;
        }
        for (EprDeactivateReasonEnum reason : EprDeactivateReasonEnum.values()) {
            if (reason.getEnName().equals(enName)) {
                return reason;
            }
        }
        return null;
    }
}
