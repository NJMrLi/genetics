package com.genetics.entity.domain;

import com.genetics.entity.Entity;
import lombok.Data;
import lombok.ToString;

/**
 * <p>
 * 
 * </p>
 *
 * @author 王影
 * @since 2023-10-12
 */
@Data
@ToString
public class CompanyBank extends Entity {

    private static final long serialVersionUID = 1L;

    /**
     * 公司ID
     */
    private Integer companyId;

    /**
     * 公司银行账户
     */
    private String bankAccount;

    /**
     * 公司IBAN账户
     */
    private String companyIban;

    /**
     * 公司SWIFT或BIC账户
     */
    private String companySwiftBic;

    /**
     * 公司银行英文地址
     */
    private String companyBankEnAddress;




}
