package com.genetics.entity.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 公司信息业务实体
 * controlKey前缀: Company
 */
@Data
@NoArgsConstructor
public class Company {
    private String companyName;
    private String companyCountry;
}
