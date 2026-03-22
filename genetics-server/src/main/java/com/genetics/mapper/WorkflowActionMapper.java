package com.genetics.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.genetics.entity.workflow.WorkflowAction;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface WorkflowActionMapper extends BaseMapper<WorkflowAction> {
}
