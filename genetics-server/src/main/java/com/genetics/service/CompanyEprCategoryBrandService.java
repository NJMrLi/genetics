package com.genetics.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.genetics.entity.domain.CompanyEprCategoryBrand;

import java.util.List;

public interface CompanyEprCategoryBrandService extends IService<CompanyEprCategoryBrand> {

    List<CompanyEprCategoryBrand> listByCompanyId(Integer companyId);
}
