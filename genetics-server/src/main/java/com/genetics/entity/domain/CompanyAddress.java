package com.genetics.entity.domain;
import com.genetics.entity.Entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * <p>
 * 公司地址信息
 * </p>
 *
 * @author litl
 * @since 2022-10-20
 */
@Data
@ToString
@EqualsAndHashCode(callSuper = true)
public class CompanyAddress extends Entity {

    private static final long serialVersionUID = 1L;

    /**
     *  公司id
     */
    private Integer companyId;

    /**
     *  省
     */
    private Integer provinceId;

    /**
     *  省编码
     */
    private String provinceCode;

    /**
     *  省名称
     */
    private String provinceName;

    /**
     *  省英文名称
     */
    private String provinceEnname;

    /**
     *  市ID
     */
    private Integer cityId;

    /**
     *  市编码
     */
    private String cityCode;

    /**
     *  市名称
     */
    private String cityName;

    /**
     *  市英文名称
     */
    private String cityEnname;

    /**
     *  区ID
     */
    private Integer areaId;

    /**
     *  区编码
     */
    private String areaCode;

    /**
     *  区名称
     */
    private String areaName;

    /**
     *  区英文名称
     */
    private String areaEnname;

    /**
     *  详细地址
     */
    private String detailAddress;

    /**
     *  详细英文地址1
     */
    private String detailEnaddress1;

    /**
     *  详细英文地址2
     */
    private String detailEnaddress2;

    /**
     *  详细英文地址3
     */
    private String detailEnaddress3;

    /**
     *  完整地址
     */
    private String completeAddress;

    /**
     *  完整英文地址
     */
    private String completeEnaddress;

    /**
     *  邮编
     */
    private String zipCode;

    /**
     *  座机
     */
    private String fixedTelephone;

    /**
     * 地址类型 1.注册地址
     */
    private Integer addressTypeId;

    /**
     * 地址类型 1.注册地址
     */
    private String addressTypeName;

}
