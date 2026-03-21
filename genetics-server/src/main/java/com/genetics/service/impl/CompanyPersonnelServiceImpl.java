package com.genetics.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.genetics.entity.domain.CompanyPersonnel;
import com.genetics.mapper.CompanyPersonnelMapper;
import com.genetics.service.CompanyPersonnelService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyPersonnelServiceImpl extends ServiceImpl<CompanyPersonnelMapper, CompanyPersonnel> implements CompanyPersonnelService {

    @Override
    public List<CompanyPersonnel> listByCompanyId(Integer companyId) {
        return list(new LambdaQueryWrapper<CompanyPersonnel>()
                .eq(CompanyPersonnel::getCompanyId, companyId));
    }
}
