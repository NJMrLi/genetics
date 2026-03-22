package com.genetics.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.genetics.entity.workflow.WorkflowAction;
import com.genetics.mapper.WorkflowActionMapper;
import com.genetics.service.WorkflowActionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkflowActionServiceImpl extends ServiceImpl<WorkflowActionMapper, WorkflowAction> implements WorkflowActionService {
    
    @Override
    public List<WorkflowAction> listAllOrdered() {
        return list(new LambdaQueryWrapper<WorkflowAction>()
                .orderByAsc(WorkflowAction::getSort));
    }
}
