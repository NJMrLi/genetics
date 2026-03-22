package com.genetics.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.genetics.entity.workflow.TemplateWorkflowConfig;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 服务单模板实体
 */
@Data
@TableName(value = "form_template", autoResultMap = true)
public class FormTemplate {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 服务单名称 */
    private String templateName;

    /** 版本 */
    private String version;

    /** 国家代码 DEU/FRA/ITA/ESP/POL/CZE/GBR */
    private String countryCode;

    /** 一级服务code 01/02 */
    private String serviceCodeL1;

    /** 二级服务code 0101/0201... */
    private String serviceCodeL2;

    /** 三级服务code 010101/020101... */
    private String serviceCodeL3;

    /**
     * 画板定义的JSON Schema
     * {"layout":"grid","columns":2,"rows":[...]}
     */
    private String jsonSchema;

    /**
     * 工作流配置
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private TemplateWorkflowConfig workflowConfig;

    /** 状态: 0草稿 1发布 */
    private Integer status;

    /** 备注 */
    private String remark;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Integer deleted;
}
