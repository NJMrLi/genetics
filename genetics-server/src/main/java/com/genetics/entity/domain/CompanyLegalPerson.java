package com.genetics.entity.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 公司法人信息业务实体
 * controlKey前缀: CompanyLegalPerson
 */
@Data
@NoArgsConstructor
public class CompanyLegalPerson {
    private String companyLegalName;
    private String companyLegalAddress;
}
