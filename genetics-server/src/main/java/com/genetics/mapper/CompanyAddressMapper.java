package com.genetics.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.genetics.entity.domain.CompanyAddress;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CompanyAddressMapper extends BaseMapper<CompanyAddress> {
}
