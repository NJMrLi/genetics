package com.genetics.entity.domain;


import com.genetics.entity.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * <p>
 * 公司联系人信息
 * </p>
 *
 * @author litl
 * @since 2022-10-20
 */
@Data
@EqualsAndHashCode(callSuper=true)
@ToString
public class CompanyContact extends Entity {

    private static final long serialVersionUID = 1L;

    /**
     * 公司id
     */
    private Integer companyId;

    /**
     * 联系人姓名
     */
    private String contactPerson;

    /**
     * 联系方式(电话/手机等)
     */
    private String contactPhone;

    /**
     * 联系人电子邮箱
     */
    private String contactEmail;

    /**
     * 职务
     */
    private String contactPosition;

    /**
     * 第三方联系方式类型
     */
    private Integer otherContactType;

    /**
     * 第三方联系方式号码
     */
    private String otherContact;

    /**
     * 主要联系人
     */
    private Integer main;

    /**
     * 电话区域号码
     */
    private String phoneRegionCode;

}