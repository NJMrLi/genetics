package com.genetics.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.genetics.dto.FormInstanceCreateDTO;
import com.genetics.dto.FormInstanceDetailVO;
import com.genetics.dto.FormInstanceSaveDTO;
import com.genetics.entity.FormInstance;

import java.util.Map;

public interface FormInstanceService {

    FormInstanceDetailVO create(FormInstanceCreateDTO dto);

    void save(Long id, FormInstanceSaveDTO dto);

    /** 单独更新业务状态 */
    void updateOrderStatus(Long id, Integer orderStatusId);

    Map<String, Object> submit(Long id);

    FormInstanceDetailVO getDetailById(Long id);

    Page<FormInstance> page(int pageNum, int pageSize, Integer status, Integer orderStatusId);
}
