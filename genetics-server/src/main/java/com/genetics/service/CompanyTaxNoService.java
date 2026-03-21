package com.genetics.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.genetics.entity.domain.CompanyTaxNo;

import java.util.List;

public interface CompanyTaxNoService extends IService<CompanyTaxNo> {

    List<CompanyTaxNo> listByCompanyId(Integer companyId);
}
