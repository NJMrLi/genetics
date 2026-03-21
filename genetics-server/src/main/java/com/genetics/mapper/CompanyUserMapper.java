package com.genetics.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.genetics.entity.domain.CompanyUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CompanyUserMapper extends BaseMapper<CompanyUser> {
}
