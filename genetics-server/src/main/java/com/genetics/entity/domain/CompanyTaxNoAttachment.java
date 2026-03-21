package com.genetics.entity.domain;

import com.genetics.entity.AttachmentBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * <p>
 * 公司税号附件
 * </p>
 *
 * @author litl
 * @since 2022-10-20
 */
@Data
@EqualsAndHashCode(callSuper=true)
@ToString
public class CompanyTaxNoAttachment extends AttachmentBaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 公司税号ID
     */
    private Integer companyTaxNoId;

}
