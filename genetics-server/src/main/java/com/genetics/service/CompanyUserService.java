package com.genetics.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.genetics.entity.domain.CompanyUser;

import java.util.List;

public interface CompanyUserService extends IService<CompanyUser> {

    List<CompanyUser> listByCompanyId(Integer companyId);
}
