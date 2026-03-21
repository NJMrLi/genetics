package com.genetics.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.genetics.entity.domain.CompanyBank;
import com.genetics.mapper.CompanyBankMapper;
import com.genetics.service.CompanyBankService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyBankServiceImpl extends ServiceImpl<CompanyBankMapper, CompanyBank> implements CompanyBankService {

    @Override
    public List<CompanyBank> listByCompanyId(Integer companyId) {
        return list(new LambdaQueryWrapper<CompanyBank>()
                .eq(CompanyBank::getCompanyId, companyId));
    }
}
