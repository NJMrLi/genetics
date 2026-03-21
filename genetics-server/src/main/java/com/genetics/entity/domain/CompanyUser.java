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
public class CompanyUser extends Entity {

    private static final long serialVersionUID = 1L;

    /**
     * 公司ID
     */
    private Integer companyId;

    /**
     * 用户数据ID(user.id)
     */
    private Integer userId;

    /**
     * 客户账号
     */
    private String userAccount;

    /**
     * 手机号码
     */
    private String userMobile;

    /**
     * 邮箱
     */
    private String userEmail;

}
