package com.genetics.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.genetics.entity.domain.CompanyOverseaManage;

import java.util.List;

public interface CompanyOverseaManageService extends IService<CompanyOverseaManage> {

    List<CompanyOverseaManage> listByCompanyId(Integer companyId);
}
