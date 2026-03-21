package com.genetics.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.genetics.dto.FormControlDTO;
import com.genetics.entity.FormControl;

import java.util.List;
import java.util.Map;

public interface FormControlService {

    Long save(FormControlDTO dto);

    void update(Long id, FormControlDTO dto);

    void delete(Long id);

    FormControl getById(Long id);

    Page<FormControl> page(int pageNum, int pageSize, String controlType, String businessType, String keyword);

    List<FormControl> listAll();

    /** 按业务类型筛选控件 */
    List<FormControl> listByBusinessType(String businessType);

    /** 获取按业务类型分组的控件列表 */
    Map<String, List<FormControl>> listGroupedByBusinessType();

    /** 获取所有业务类型列表 */
    List<String> listAllBusinessTypes();

    List<FormControl> listByIds(List<Long> ids);
}
