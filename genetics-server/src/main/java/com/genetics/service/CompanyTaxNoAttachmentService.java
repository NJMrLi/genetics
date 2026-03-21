package com.genetics.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.genetics.entity.domain.CompanyTaxNoAttachment;

import java.util.List;

public interface CompanyTaxNoAttachmentService extends IService<CompanyTaxNoAttachment> {

    List<CompanyTaxNoAttachment> listByCompanyTaxNoId(Integer companyTaxNoId);
}
