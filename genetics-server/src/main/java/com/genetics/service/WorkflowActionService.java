package com.genetics.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.genetics.entity.workflow.WorkflowAction;
import java.util.List;

public interface WorkflowActionService extends IService<WorkflowAction> {
    /**
     * 获取所有可用的动作列表（按排序排序）
     */
    List<WorkflowAction> listAllOrdered();
}
