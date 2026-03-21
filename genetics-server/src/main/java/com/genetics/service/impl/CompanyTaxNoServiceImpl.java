package com.genetics.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.genetics.entity.domain.CompanyTaxNo;
import com.genetics.mapper.CompanyTaxNoMapper;
import com.genetics.service.CompanyTaxNoService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyTaxNoServiceImpl extends ServiceImpl<CompanyTaxNoMapper, CompanyTaxNo> implements CompanyTaxNoService {

    @Override
    public List<CompanyTaxNo> listByCompanyId(Integer companyId) {
        return list(new LambdaQueryWrapper<CompanyTaxNo>()
                .eq(CompanyTaxNo::getCompanyId, companyId));
    }
}
