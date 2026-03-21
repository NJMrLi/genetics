package com.genetics.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.genetics.entity.domain.CompanyEprCategoryBrand;
import com.genetics.mapper.CompanyEprCategoryBrandMapper;
import com.genetics.service.CompanyEprCategoryBrandService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyEprCategoryBrandServiceImpl extends ServiceImpl<CompanyEprCategoryBrandMapper, CompanyEprCategoryBrand> implements CompanyEprCategoryBrandService {

    @Override
    public List<CompanyEprCategoryBrand> listByCompanyId(Integer companyId) {
        return list(new LambdaQueryWrapper<CompanyEprCategoryBrand>()
                .eq(CompanyEprCategoryBrand::getCompanyId, companyId));
    }
}
