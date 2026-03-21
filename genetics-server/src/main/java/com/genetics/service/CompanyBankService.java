package com.genetics.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.genetics.entity.domain.CompanyBank;

import java.util.List;

public interface CompanyBankService extends IService<CompanyBank> {

    List<CompanyBank> listByCompanyId(Integer companyId);
}
