package com.genetics.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Map;

/**
 * 创建服务单实例DTO
 */
@Data
public class FormInstanceCreateDTO {

    @NotNull(message = "模板ID不能为空")
    private Long templateId;
}
