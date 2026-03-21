package com.genetics.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.genetics.entity.domain.CompanyUser;
import com.genetics.mapper.CompanyUserMapper;
import com.genetics.service.CompanyUserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyUserServiceImpl extends ServiceImpl<CompanyUserMapper, CompanyUser> implements CompanyUserService {

    @Override
    public List<CompanyUser> listByCompanyId(Integer companyId) {
        return list(new LambdaQueryWrapper<CompanyUser>()
                .eq(CompanyUser::getCompanyId, companyId));
    }
}
