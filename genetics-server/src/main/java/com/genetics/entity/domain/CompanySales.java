package com.genetics.entity.domain;

import com.genetics.entity.Entity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 公司店铺信息表
 * </p>
 *
 * @author 王影
 * @since 2023-10-12
 */
@Getter
@Setter
@ToString
public class CompanySales extends Entity {

    private static final long serialVersionUID = 1L;

    /**
     * 公司ID
     */
    private Integer companyId;

    /**
     * 第三方id
     */
    private Integer saleItem;

    /**
     * 销售平台id
     */
    private Integer salePlatformId;

    /**
     * 销售平台
     */
    private String salePlatformName;

    /**
     * 店铺ID
     */
    private String shopId;

    private String shopName;

    /**
     * 店铺url
     */
    private String shopUrl;

    /**
     * 经营范围
     */
    private String businessScope;

    /**
     * 经营范围id
     */
    private Integer businessScopeId;

    /**
     * 经营范围type
     */
    private String businessScopeCode;

    /**
     * 预计年销售量（万元）
     */
    private BigDecimal saleYearAmount;

    /**
     * 数据来源 0：服务单，1：手动创建
     */
    private Integer source;

    /**
     * 授权状态 0：未授权，1：已授权，2：已失效
     */
    private Integer authStatus;

    /**
     * 授权时间
     */
    private LocalDateTime authTime;

    /**
     * 授权预计过期时间
     */
    private LocalDateTime authExpectedExpiry;

    /**
     * 当地仓库地址
     */
    private String stockAddress;
}
