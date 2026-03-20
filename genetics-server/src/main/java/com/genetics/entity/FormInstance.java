package com.genetics.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 服务单实例实体
 */
@Data
@TableName("form_instance")
public class FormInstance {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 关联模板ID */
    private Long templateId;

    /** 服务单名称（冗余） */
    private String templateName;

    /** 版本（冗余） */
    private String version;

    /** 国家代码（冗余） */
    private String countryCode;

    /** 一级服务code */
    private String serviceCodeL1;

    /** 二级服务code */
    private String serviceCodeL2;

    /** 三级服务code */
    private String serviceCodeL3;

    /**
     * 表单数据 Map<controlKey, value> 序列化JSON
     * {"Company.companyName":"测试公司", "Company.companyCountry":"DEU"}
     */
    private String formData;

    /** 表单流转状态: 0草稿 1已提交 2已审核 */
    private Integer status;

    /**
     * 业务处理状态ID
     * 10待提交 20待审核 30待递交 31组织处理 32税局处理 33当地同事处理 40已完成 50已驳回 99已终止
     */
    private Integer orderStatusId;

    /** 服务开始时间 */
    private LocalDateTime serviceStartTime;

    /** 服务结束时间 */
    private LocalDateTime serviceEndTime;

    /** 提交时间 */
    private LocalDateTime submitTime;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Integer deleted;
}
