package com.genetics.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.genetics.entity.domain.CompanyContact;

import java.util.List;

public interface CompanyContactService extends IService<CompanyContact> {

    List<CompanyContact> listByCompanyId(Integer companyId);
}
