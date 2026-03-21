package com.genetics.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.genetics.entity.domain.CompanyAddress;

import java.util.List;

public interface CompanyAddressService extends IService<CompanyAddress> {

    List<CompanyAddress> listByCompanyId(Integer companyId);
}
