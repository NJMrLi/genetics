package com.genetics.entity.domain;

import com.genetics.entity.Entity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDate;

/**
 * <p>
 * 公司信息
 * </p>
 *
 * @author litl
 * @since 2022-10-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString
public class Company extends Entity {

    /**
     * 公司编码
     */
    private String code;

    /**
     * 公司名称
     */
    private String name;

    /**
     * 公司英文名称
     */
    private String enname;

    /**
     * 公司简称
     */
    private String abbr;

    /**
     * 公司邮箱
     */
    private String email;

    /**
     * 公司联系电话
     */
    private String fixedTelephone;

    /**
     * 国家地区id
     */
    private Integer regionId;

    /**
     * 国家地区编码
     */
    private String regionCode;

    /**
     * 国家地区编码
     */
    private String regionCode2;

    /**
     * 国家地区名称
     */
    private String regionName;

    /**
     * 类型：1营业执照2注册证书
     */
    private Integer typeId;

    /**
     * 类型：1营业执照2注册证书
     */
    private String typeName;

    /**
     * 注册资本
     */
    private String registeredCapital;

    /**
     * 注册资本币种名称
     */
    private String currencyName;

    /**
     * 注册资本币种code
     */
    private String currencyCode;

    /**
     * 公司注册地
     */
    private String registeredArea;

    /**
     * 公司注册代码
     */
    private String registerCode;

    /**
     * 统一社会信用代码
     */
    private String socialCreditCode;

    /**
     * 企业类型ID
     */
    private Integer econKindId;

    /**
     * 企业类型名称
     */
    private String econKindName;

    /**
     * 企业类型英文名称
     */
    private String econKindEnname;

    /**
     * 成立日期
     */
    private String startDate;

    private String issuedDate;

    /**
     * 德国本土税号
     */
    @Schema(description = "本土税号")
    private String gerLocalTaxNumber;

    /**
     * 本国VAT税号
     */
    private String registryCountryTax;

    /**
     * 公司银行账号，德国独有字段，英国无
     */
    private String companyBankAccount;

    /**
     * 公司IBAN
     */
    private String companyIban;

    /**
     * 公司SWIFT BIC
     */
    private String companySwiftBic;

    private String businessScope;

    /**
     * 1：启用 0:停用
     */
    private Boolean enabled;

    /**
     * companyBank Name
     * 公司银行的英文名称
     *
     * @return
     */
    @Schema(description = "公司银行英文名称")
    private String companyBankEnName;
    /**
     * companyBank Address
     * 公司银行的英文地址
     *
     * @return
     */
    @Schema(description = "公司银行英文地址")
    private String companyBankEnAddress;

    /**
     * 联系人姓名
     */
    @Schema(description = "联系人姓名")
    private String contactPerson;
    /**
     * 联系人电话区域代码
     */
    private String phoneRegionCode;
    /**
     * 联系方式(电话/手机等)
     */
    @Schema(description = "联系人联系方式")
    private String contactPhone;

    /**
     * 联系人电子邮箱
     */
    @Schema(description = "联系人电子邮箱")
    private String contactEmail;

    /**
     * 职务
     */
    @Schema(description = "职务")
    private String contactPosition;

    @Schema(description = "欧盟Eori注册国")
    private String eoriCountryCode;

    @Schema(description = "欧盟Eori")
    private String eoriEu;

    @Schema(description = "英国EORI")
    private String eoriEn;

    @Schema(description = "法国sepa")
    private Integer sepa;

    @Schema(description = "进口记录类型：BUYER或SELLER")
    private String importOfRecord;

    @Schema(description = "财年结束日期")
    private LocalDate fiscalYearEndDate;
}
