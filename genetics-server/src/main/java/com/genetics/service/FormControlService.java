package com.genetics.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.genetics.dto.FormControlDTO;
import com.genetics.entity.FormControl;

import java.util.List;

public interface FormControlService {

    Long save(FormControlDTO dto);

    void update(Long id, FormControlDTO dto);

    void delete(Long id);

    FormControl getById(Long id);

    Page<FormControl> page(int pageNum, int pageSize, String controlType, String keyword);

    List<FormControl> listAll();

    List<FormControl> listByIds(List<Long> ids);
}
