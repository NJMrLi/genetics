package com.genetics.controller;

import com.genetics.common.PageResult;
import com.genetics.common.Result;
import com.genetics.dto.FormTemplateDTO;
import com.genetics.dto.FormTemplateDetailVO;
import com.genetics.entity.FormTemplate;
import com.genetics.service.FormTemplateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 服务单模板管理接口
 */
@RestController
@RequestMapping("/api/form-template")
@RequiredArgsConstructor
public class FormTemplateController {

    private final FormTemplateService formTemplateService;

    @PostMapping
    public Result<Map<String, Long>> save(@Valid @RequestBody FormTemplateDTO dto) {
        Long id = formTemplateService.save(dto);
        return Result.success(Map.of("id", id));
    }

    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @Valid @RequestBody FormTemplateDTO dto) {
        formTemplateService.update(id, dto);
        return Result.success();
    }

    @PostMapping("/{id}/publish")
    public Result<Void> publish(@PathVariable Long id) {
        formTemplateService.publish(id);
        return Result.success();
    }

    @PostMapping("/{id}/upgrade")
    public Result<Map<String, Long>> upgrade(@PathVariable Long id) {
        Long newId = formTemplateService.upgrade(id);
        return Result.success(Map.of("id", newId));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        formTemplateService.delete(id);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<FormTemplateDetailVO> getById(@PathVariable Long id) {
        return Result.success(formTemplateService.getDetailById(id));
    }

    @GetMapping("/list")
    public Result<PageResult<FormTemplate>> page(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String countryCode,
            @RequestParam(required = false) String serviceCodeL3,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false, defaultValue = "false") Boolean latestOnly) {
        return Result.success(PageResult.of(formTemplateService.page(page, size, countryCode, serviceCodeL3, status, latestOnly)));
    }
}
