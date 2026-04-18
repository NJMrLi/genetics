package com.genetics.controller;

import com.genetics.common.PageResult;
import com.genetics.common.Result;
import com.genetics.dto.FormInstanceCreateDTO;
import com.genetics.dto.FormInstanceDetailVO;
import com.genetics.dto.FormInstanceSaveDTO;
import com.genetics.dto.WorkflowTransitionRequestDTO;
import com.genetics.entity.FormInstance;
import com.genetics.entity.workflow.WorkflowTransition;
import com.genetics.enums.ServeState;
import com.genetics.service.FormInstanceService;
import com.genetics.service.TemplateWorkflowService;
import com.genetics.workflow.WorkflowActionDispatcher;
import com.genetics.workflow.action.WorkflowActionResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 服务单实例接口
 */
@RestController
@RequestMapping("/api/form-instance")
@RequiredArgsConstructor
public class FormInstanceController {

    private final FormInstanceService formInstanceService;
    private final TemplateWorkflowService templateWorkflowService;
    private final WorkflowActionDispatcher actionDispatcher;

    /**
     * 根据模板创建服务单实例
     */
    @PostMapping("/create")
    public Result<FormInstanceDetailVO> create(@Valid @RequestBody FormInstanceCreateDTO dto) {
        return Result.success(formInstanceService.create(dto));
    }

    /**
     * 保存服务单草稿数据（含业务状态、服务时间）
     */
    @PutMapping("/{id}/save")
    public Result<Void> save(@PathVariable Long id, @Valid @RequestBody FormInstanceSaveDTO dto) {
        formInstanceService.save(id, dto);
        return Result.success();
    }
    
    /**
     * 统一动作执行接口
     * 通过 action 参数分发到对应插件执行
     * 
     * @param id 服务单实例ID
     * @param request 动作执行请求 (包含 action code, remark, formData)
     * @return 动作执行结果
     */
    @PostMapping("/{id}/execute")
    public Result<WorkflowActionResult> executeAction(
            @PathVariable Long id,
            @RequestBody WorkflowTransitionRequestDTO request) {
        // Dispatcher 内部会更新数据库
        WorkflowActionResult result = actionDispatcher.dispatch(id, request);
        return Result.success(result);
    }

    /**
     * 获取服务单详情（含模板schema和已填数据）
     */
    @GetMapping("/{id}")
    public Result<FormInstanceDetailVO> getById(@PathVariable Long id) {
        return Result.success(formInstanceService.getDetailById(id));
    }

    /**
     * 服务单列表（支持按 orderStatusId 筛选）
     */
    @GetMapping("/list")
    public Result<PageResult<FormInstance>> page(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Integer orderStatusId) {
        return Result.success(PageResult.of(formInstanceService.page(page, size, status, orderStatusId)));
    }

    /**
     * 获取业务状态枚举列表（供前端下拉使用）
     */
    @GetMapping("/order-status/options")
    public Result<List<Map<String, Object>>> orderStatusOptions() {
        List<Map<String, Object>> options = Arrays.stream(ServeState.values())
                .map(s -> Map.<String, Object>of(
                        "code", s.getId(),
                        "name", s.getName(),
                        "tagType", s.getTagType()
                ))
                .collect(Collectors.toList());
        return Result.success(options);
    }

    /**
     * 获取当前实例可用的操作列表
     */
    @GetMapping("/{id}/available-actions")
    public Result<List<WorkflowTransition>> getAvailableActions(@PathVariable Long id) {
        FormInstanceDetailVO vo = formInstanceService.getDetailById(id);
        List<WorkflowTransition> actions = templateWorkflowService.getInstanceAvailableActions(
                convertToEntity(vo));
        return Result.success(actions);
    }

    private FormInstance convertToEntity(FormInstanceDetailVO vo) {
        FormInstance instance = new FormInstance();
        instance.setId(vo.getInstanceId());
        instance.setTemplateId(vo.getTemplateId());
        instance.setCountryCode(vo.getCountryCode());
        instance.setServiceCodeL1(vo.getServiceCodeL1());
        instance.setOrderStatusId(vo.getOrderStatusId());
        return instance;
    }
}
