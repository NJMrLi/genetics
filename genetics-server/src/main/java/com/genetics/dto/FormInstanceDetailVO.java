package com.genetics.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 服务单实例详情VO（含模板schema和控件详情，用于前端渲染）
 */
@Data
public class FormInstanceDetailVO {

    private Long instanceId;
    private Long templateId;
    private String templateName;
    private String version;
    private String countryCode;
    private String serviceCodeL1;
    private String serviceCodeL2;
    private String serviceCodeL3;
    /** 解析后的JSON Schema */
    private Object jsonSchema;
    /** 控件详情列表 */
    private List<FormTemplateDetailVO.ControlDetailVO> controlDetails;
    /** 当前表单数据（已填写内容） */
    private Map<String, Object> formData;
    /** 表单流转状态 0草稿 1已提交 2已审核 */
    private Integer status;
    /** 业务处理状态ID */
    private Integer orderStatusId;
    /** 业务状态名称（冗余，方便前端展示） */
    private String orderStatusName;
    /** 服务开始时间 */
    private LocalDateTime serviceStartTime;
    /** 服务结束时间 */
    private LocalDateTime serviceEndTime;
    private LocalDateTime submitTime;
    private LocalDateTime createTime;
}
