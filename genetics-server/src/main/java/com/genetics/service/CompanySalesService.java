package com.genetics.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.genetics.entity.domain.CompanySales;

import java.util.List;

public interface CompanySalesService extends IService<CompanySales> {

    List<CompanySales> listByCompanyId(Integer companyId);
}
