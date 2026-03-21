package com.genetics.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 自定义控件定义实体
 */
@Data
@TableName("form_control")
public class FormControl {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 控件名称（展示用） */
    private String controlName;

    /** 控件key，格式: ClassName.fieldName */
    private String controlKey;

    /** 控件类型: INPUT/SELECT/SWITCH/UPLOAD/TEXTAREA/DATE/NUMBER */
    private String controlType;

    /** 业务类型（实体类名）: Company/CompanyAddress等 */
    private String businessType;

    /** 占位文本 */
    private String placeholder;

    /** 控件说明(TIPS) */
    private String tips;

    /** 是否必填 */
    private Boolean required;

    /** 正则表达式约束 */
    private String regexPattern;

    /** 正则校验失败提示语 */
    private String regexMessage;

    /** 最小长度 */
    private Integer minLength;

    /** 最大长度 */
    private Integer maxLength;

    /**
     * 下拉框选项JSON字符串
     * 格式: [{"label":"中文名","value":"DEU"}]
     */
    private String selectOptions;

    /**
     * 上传配置JSON字符串（仅type=UPLOAD时有效）
     * 格式: {"maxCount":3,"accept":".pdf,.jpg","maxSizeMB":10}
     */
    private String uploadConfig;

    /** 默认值 */
    private String defaultValue;

    /** 排序 */
    private Integer sort;

    /** 是否启用 */
    private Boolean enabled;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Integer deleted;
}
