package com.genetics.controller;

import com.genetics.common.Result;
import com.genetics.entity.workflow.WorkflowAction;
import com.genetics.service.WorkflowActionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/workflow/actions")
@RequiredArgsConstructor
public class WorkflowActionController {

    private final WorkflowActionService workflowActionService;

    @GetMapping("/list")
    public Result<List<WorkflowAction>> list() {
        return Result.success(workflowActionService.listAllOrdered());
    }

    @PostMapping
    public Result<Boolean> save(@RequestBody WorkflowAction action) {
        return Result.success(workflowActionService.saveOrUpdate(action));
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.success(workflowActionService.removeById(id));
    }
}
