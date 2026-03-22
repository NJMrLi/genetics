package com.genetics.dto;

import com.genetics.entity.workflow.TemplateWorkflowConfig;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 服务单模板保存DTO
 */
@Data
public class FormTemplateDTO {

    private Long id;

    @NotBlank(message = "服务单名称不能为空")
    private String templateName;

    private String version = "1.0.0";

    @NotBlank(message = "国家代码不能为空")
    private String countryCode;

    @NotBlank(message = "一级服务code不能为空")
    private String serviceCodeL1;

    @NotBlank(message = "二级服务code不能为空")
    private String serviceCodeL2;

    @NotBlank(message = "三级服务code不能为空")
    private String serviceCodeL3;

    /** JSON Schema 对象，接口层解析为字符串存储 */
    private Object jsonSchema;

    /** 工作流配置 */
    private TemplateWorkflowConfig workflowConfig;

    private Integer status;
    private String remark;
}
