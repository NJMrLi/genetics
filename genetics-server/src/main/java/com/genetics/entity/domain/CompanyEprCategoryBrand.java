package com.genetics.entity.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;

import com.genetics.entity.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 公司品牌
 * </p>
 *
 * @author litl
 * @since 2022-10-20
 */
@Data
@ToString
@EqualsAndHashCode(callSuper = false)
@TableName(value = "company_epr_category_brand",autoResultMap = true)
public class CompanyEprCategoryBrand extends Entity {

    private static final long serialVersionUID = 1L;

    /**
     * 公司id
     */
    private Integer companyId;

    /**
     *  运营类目编码 020201
     */
    private String operationCategoryCode;

    /**
     * 二级运营类目名称 包装法
     */
    private String operationCategoryName;

    /**
     * 国家地区3位编码
     */
    private String regionCode;

    /**
     * 国家地区2位编码
     */
    private String regionCode2;

    /**
     * 国家地区名称
     */
    private String regionName;

    /**
     * 产品分类
     */
    private String categoryName;

    /**
     * 品牌名称
     */
    private String brandName;

    /**
     * 注册号
     */
    private String registrationNumber;

    /**
     * 注册状态 0-注册 1-注销
     */
    private Integer registrationStatus;

    /**
     * 指标项基础值（仅作用于分类） key指标列名称 value是阈值（基础申报值）
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private Map<String,String> indicatorBaseValueMap;

    /**
     * 产品分类列名
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> categoryColName;

}
