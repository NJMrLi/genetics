package com.genetics.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 控件保存DTO
 */
@Data
public class FormControlDTO {

    private Long id;

    @NotBlank(message = "控件名称不能为空")
    private String controlName;

    @NotBlank(message = "控件key不能为空")
    private String controlKey;

    @NotBlank(message = "控件类型不能为空")
    private String controlType;

    /** 业务类型（实体类名） */
    private String businessType;

    private String placeholder;
    private String tips;

    @NotNull(message = "是否必填不能为空")
    private Boolean required;

    private String regexPattern;
    private String regexMessage;
    private Integer minLength;
    private Integer maxLength;

    /** 下拉框选项，前端传JSON字符串 */
    private String selectOptions;

    /** 上传配置，前端传JSON字符串 */
    private String uploadConfig;

    private String defaultValue;
    private Integer sort;
    private Boolean enabled;
}
