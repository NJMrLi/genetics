package com.genetics.entity.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;

import com.genetics.entity.Entity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * <p>
 * 公司税号
 * </p>
 *
 * @author litl
 * @since 2022-12-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString
@TableName(value = "company_tax_no", autoResultMap = true)
public class CompanyTaxNo extends Entity {

    private static final long serialVersionUID = 1L;
    /**
     * 公司id
     */
    private Integer companyId;

    /**
     * 国家id
     */
    private Integer countryId;

    /**
     * 国家编码
     */
    private String countryCode;

    /**
     * 国家名称
     */
    private String countryName;

    /**
     * 国家英文名称
     */
    private String countryEnname;

    /**
     * 法案code
     */
    private String serviceCode;

    /**
     * 法案名称
     */
    private String serviceName;

    /**
     * 税号
     */
    private String taxNo;

    /**
     * 1.VAT税号2.VAT税务识别号3.包装法注册号
     */
    private Integer taxNoTypeId;

    /**
     * 1.VAT税号2.VAT税务识别号3.包装法注册号
     */
    private String taxNoTypeName;

    /**
     * 1无服务 10 服务未开始 20 服务中 30 服务过期 40 已终止
     */
    private Integer statusId;

    /**
     * 1无服务 10 服务未开始 20 服务中 30 服务过期 40 已终止
     */
    private String statusName;

    /**
     * 10未授权20已授权30无授权
     */
    private Integer authStatusId;

    /**
     * 10未授权20已授权30无授权
     */
    private String authStatusName;

    /**
     * 服务开始时间
     */
    private LocalDate serviceStartTime;

    /**
     * 服务结束时间
     */
    private LocalDate serviceEndTime;

    /**
     * 授权开始时间
     */
    private LocalDate authStartTime;

    /**
     * 授权失效时间
     */
    private LocalDate authEndTime;

    /**
     * 账号
     */
    private String account;

    /**
     * 密码
     */
    private String password;

    /**
     * 英国HMRC CDS账号
     */
    private String hmrcCdsAccount;

    /**
     * 英国HMRC CDS密码
     */
    private String hmrcCdsPassword;

    /**
     * 验证手机号
     */
    private String phone;

    /**
     * 刷新token
     */
    private String refreshToken;

    /**
     * 授权标识
     */
    private String state;

    /**
     * 1.季度申报2.月申报3.年申报
     */
    private Integer declarationCycleId;

    /**
     * 1.季度申报2.月申报3.年申报
     */
    private String declarationCycleName;

    /**
     * 是否生成申报义务标志(0：未生成，1：已生成)
     */
    private Boolean hasDeclarationObligation;

    /**
     * 付款备注
     */
    private String paymentRemarks;

    /**
     * 下号日期
     */
    private LocalDate signUnderDate;

    /**
     * 提交资料日期
     */
    private LocalDate submitInformationDate;

    /**
     * 本土税号(德国vat用)
     */
    private String localTaxNumber;

    /**
     * 税率
     */
    private BigDecimal taxRate;

    /**
     * 税号生效时间
     */
    private LocalDate vrnEffectiveTime;

    /**
     * 税号注销 0否 1是
     */
    private Integer vrnHasCancelled;

    /**
     * 税号状态 1、正常 2、终止
     */
    private Integer vrnActiveStatus;

    /**
     * 税号提供方TypeId
     */
    private Integer serviceProviderType;

    /**
     * 税号提供方Name
     */
    private String serviceProviderName;

    /**
     * 变更原因
     */
    private String changeReason;


    /**
     * 回收组织Id
     */
    @Schema(description = "回收组织Id")
    private Integer recoveryOrganizeId;

    /**
     * 回收组织Name
     */
    @Schema(description = "回收组织Name")
    private String recoveryOrganizeName;

    /**
     * 收款信息(JSON格式，包含iban等字段)
     */
    @Schema(description = "收款信息")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private PaymentInfo paymentInfo;

    @Data
    public static class PaymentInfo implements Serializable {

        private static final long serialVersionUID = 1L;

        /**
         * IBAN账号（国际银行账号）
         */
        @Schema(description = "IBAN账号")
        private String iban;
    }

}
