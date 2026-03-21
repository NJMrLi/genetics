package com.genetics.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.genetics.entity.domain.CompanyContact;
import com.genetics.mapper.CompanyContactMapper;
import com.genetics.service.CompanyContactService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyContactServiceImpl extends ServiceImpl<CompanyContactMapper, CompanyContact> implements CompanyContactService {

    @Override
    public List<CompanyContact> listByCompanyId(Integer companyId) {
        return list(new LambdaQueryWrapper<CompanyContact>()
                .eq(CompanyContact::getCompanyId, companyId));
    }
}
