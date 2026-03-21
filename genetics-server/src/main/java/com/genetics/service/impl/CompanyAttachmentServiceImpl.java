package com.genetics.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.genetics.entity.domain.CompanyAttachment;
import com.genetics.mapper.CompanyAttachmentMapper;
import com.genetics.service.CompanyAttachmentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyAttachmentServiceImpl extends ServiceImpl<CompanyAttachmentMapper, CompanyAttachment> implements CompanyAttachmentService {

    @Override
    public List<CompanyAttachment> listByCompanyId(Integer companyId) {
        return list(new LambdaQueryWrapper<CompanyAttachment>()
                .eq(CompanyAttachment::getCompanyId, companyId));
    }
}
