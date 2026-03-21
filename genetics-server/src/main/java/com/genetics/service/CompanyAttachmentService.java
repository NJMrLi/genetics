package com.genetics.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.genetics.entity.domain.CompanyAttachment;

import java.util.List;

public interface CompanyAttachmentService extends IService<CompanyAttachment> {

    List<CompanyAttachment> listByCompanyId(Integer companyId);
}
