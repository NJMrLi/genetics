package com.genetics.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.genetics.entity.domain.CompanyAddress;
import com.genetics.mapper.CompanyAddressMapper;
import com.genetics.service.CompanyAddressService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyAddressServiceImpl extends ServiceImpl<CompanyAddressMapper, CompanyAddress> implements CompanyAddressService {

    @Override
    public List<CompanyAddress> listByCompanyId(Integer companyId) {
        return list(new LambdaQueryWrapper<CompanyAddress>()
                .eq(CompanyAddress::getCompanyId, companyId));
    }
}
