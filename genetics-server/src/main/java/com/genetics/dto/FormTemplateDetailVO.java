package com.genetics.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 模板详情VO（含控件详情列表）
 */
@Data
public class FormTemplateDetailVO {

    private Long id;
    private String templateName;
    private String version;
    private String countryCode;
    private String serviceCodeL1;
    private String serviceCodeL2;
    private String serviceCodeL3;
    /** 解析后的Schema对象 */
    private Object jsonSchema;
    private Integer status;
    private String remark;

    /** 该模板所有控件的详情（含校验规则、上传配置等） */
    private List<ControlDetailVO> controlDetails;

    @Data
    public static class ControlDetailVO {
        private Long controlId;
        private String controlKey;
        private String controlType;
        private String controlName;
        private String placeholder;
        private String tips;
        private Boolean required;
        private String regexPattern;
        private String regexMessage;
        private Integer minLength;
        private Integer maxLength;
        /** 下拉选项（已解析） */
        private Object selectOptions;
        /** 上传配置（已解析） */
        private Object uploadConfig;
        private String defaultValue;
    }
}
