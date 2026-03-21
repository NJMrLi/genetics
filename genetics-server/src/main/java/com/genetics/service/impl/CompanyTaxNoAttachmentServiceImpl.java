package com.genetics.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.genetics.entity.domain.CompanyTaxNoAttachment;
import com.genetics.mapper.CompanyTaxNoAttachmentMapper;
import com.genetics.service.CompanyTaxNoAttachmentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyTaxNoAttachmentServiceImpl extends ServiceImpl<CompanyTaxNoAttachmentMapper, CompanyTaxNoAttachment> implements CompanyTaxNoAttachmentService {

    @Override
    public List<CompanyTaxNoAttachment> listByCompanyTaxNoId(Integer companyTaxNoId) {
        return list(new LambdaQueryWrapper<CompanyTaxNoAttachment>()
                .eq(CompanyTaxNoAttachment::getCompanyTaxNoId, companyTaxNoId));
    }
}
