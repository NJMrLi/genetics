package com.genetics.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.genetics.entity.domain.CompanyPersonnel;

import java.util.List;

public interface CompanyPersonnelService extends IService<CompanyPersonnel> {

    List<CompanyPersonnel> listByCompanyId(Integer companyId);
}
