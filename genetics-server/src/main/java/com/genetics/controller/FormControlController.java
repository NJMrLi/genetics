package com.genetics.controller;

import com.genetics.common.PageResult;
import com.genetics.common.Result;
import com.genetics.dto.FormControlDTO;
import com.genetics.entity.FormControl;
import com.genetics.service.FormControlService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 自定义控件管理接口
 */
@RestController
@RequestMapping("/api/form-control")
@RequiredArgsConstructor
public class FormControlController {

    private final FormControlService formControlService;

    @PostMapping
    public Result<Map<String, Long>> save(@Valid @RequestBody FormControlDTO dto) {
        Long id = formControlService.save(dto);
        return Result.success(Map.of("id", id));
    }

    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @Valid @RequestBody FormControlDTO dto) {
        formControlService.update(id, dto);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        formControlService.delete(id);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<FormControl> getById(@PathVariable Long id) {
        return Result.success(formControlService.getById(id));
    }

    @GetMapping("/list")
    public Result<PageResult<FormControl>> page(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String controlType,
            @RequestParam(required = false) String businessType,
            @RequestParam(required = false) String keyword) {
        return Result.success(PageResult.of(formControlService.page(page, size, controlType, businessType, keyword)));
    }

    @GetMapping("/all")
    public Result<List<FormControl>> listAll() {
        return Result.success(formControlService.listAll());
    }

    /**
     * 按业务类型筛选控件
     */
    @GetMapping("/by-business-type")
    public Result<List<FormControl>> listByBusinessType(
            @RequestParam(required = false) String businessType) {
        return Result.success(formControlService.listByBusinessType(businessType));
    }

    /**
     * 获取按业务类型分组的控件列表
     */
    @GetMapping("/grouped")
    public Result<Map<String, List<FormControl>>> listGroupedByBusinessType() {
        return Result.success(formControlService.listGroupedByBusinessType());
    }

    /**
     * 获取所有业务类型列表
     */
    @GetMapping("/business-types")
    public Result<List<String>> listAllBusinessTypes() {
        return Result.success(formControlService.listAllBusinessTypes());
    }
}
