package com.genetics.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.genetics.dto.FormInstanceCreateDTO;
import com.genetics.dto.FormInstanceDetailVO;
import com.genetics.dto.FormInstanceSaveDTO;
import com.genetics.dto.WorkflowTransitionRequestDTO;
import com.genetics.entity.FormInstance;

import java.util.Map;

public interface FormInstanceService {

    FormInstanceDetailVO create(FormInstanceCreateDTO dto);

    void save(Long id, FormInstanceSaveDTO dto);

    /**
     * 执行状态流转
     * @param id 实例ID
     * @param request 执行流转的请求数据 (action, remark, actionFormData)
     */
    void executeTransition(Long id, WorkflowTransitionRequestDTO request);

    FormInstanceDetailVO getDetailById(Long id);

    Page<FormInstance> page(int pageNum, int pageSize, Integer status, Integer orderStatusId);
}
