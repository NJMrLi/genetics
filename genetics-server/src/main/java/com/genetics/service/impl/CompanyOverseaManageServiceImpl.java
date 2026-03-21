package com.genetics.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.genetics.entity.domain.CompanyOverseaManage;
import com.genetics.mapper.CompanyOverseaManageMapper;
import com.genetics.service.CompanyOverseaManageService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyOverseaManageServiceImpl extends ServiceImpl<CompanyOverseaManageMapper, CompanyOverseaManage> implements CompanyOverseaManageService {

    @Override
    public List<CompanyOverseaManage> listByCompanyId(Integer companyId) {
        return list(new LambdaQueryWrapper<CompanyOverseaManage>()
                .eq(CompanyOverseaManage::getCompanyId, companyId));
    }
}
