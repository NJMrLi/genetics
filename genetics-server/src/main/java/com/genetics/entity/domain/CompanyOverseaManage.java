package com.genetics.entity.domain;

import com.genetics.entity.Entity;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 公司海外经营方式表
 * </p>
 *
 * @author 王影
 * @since 2023-11-09
 */
@Data
@ToString
public class CompanyOverseaManage extends Entity {

    private static final long serialVersionUID = 1L;

    /**
     * 公司用户id
     */
    private Integer companyId;


    /**
     * 公司注册号
     */
    private String registrationNumber;

    /**
     * 一级运营类目编码
     */
    private String operationCategoryCode1;

    /**
     * 一级运营类目名称
     */
    private String operationCategoryName1;

    /**
     * 二级运营类目编码
     */
    private String operationCategoryCode2;

    /**
     * 二级运营类目名称
     */
    private String operationCategoryName2;

    /**
     * 国家地区名称
     */
    private String regionName;

    /**
     * 国家地区2位编码
     */
    private String regionCode2;

    /**
     * 国家地区3位编码
     */
    private String regionCode;

    /**
     * 海外经营方式id
     */
    private Integer overseaModeId;

    /**
     * 海外经营方式名称
     */
    private String overseaModeName;

}
