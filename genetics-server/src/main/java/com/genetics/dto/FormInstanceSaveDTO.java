package com.genetics.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 保存服务单数据DTO
 */
@Data
public class FormInstanceSaveDTO {

    @NotNull(message = "表单数据不能为空")
    private Map<String, Object> formData;

    /**
     * 业务状态ID（可选）
     * 10待提交 20待审核 30待递交 31组织处理 32税局处理 33当地同事处理 40已完成 50已驳回 99已终止
     */
    private Integer orderStatusId;

    /** 服务开始时间（可选） */
    private LocalDateTime serviceStartTime;

    /** 服务结束时间（可选） */
    private LocalDateTime serviceEndTime;
}
