package com.genetics.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.genetics.dto.FormTemplateDTO;
import com.genetics.dto.FormTemplateDetailVO;
import com.genetics.entity.FormTemplate;

public interface FormTemplateService {

    Long save(FormTemplateDTO dto);

    void update(Long id, FormTemplateDTO dto);

    void publish(Long id);

    void delete(Long id);

    FormTemplate getById(Long id);

    FormTemplateDetailVO getDetailById(Long id);

    Page<FormTemplate> page(int pageNum, int pageSize, String countryCode, String serviceCodeL3);
}
