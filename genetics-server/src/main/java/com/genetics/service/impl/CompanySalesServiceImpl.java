package com.genetics.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.genetics.entity.domain.CompanySales;
import com.genetics.mapper.CompanySalesMapper;
import com.genetics.service.CompanySalesService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanySalesServiceImpl extends ServiceImpl<CompanySalesMapper, CompanySales> implements CompanySalesService {

    @Override
    public List<CompanySales> listByCompanyId(Integer companyId) {
        return list(new LambdaQueryWrapper<CompanySales>()
                .eq(CompanySales::getCompanyId, companyId));
    }
}
