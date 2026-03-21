package com.genetics.entity.domain;

import com.genetics.entity.AttachmentBaseEntity;
import com.genetics.entity.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * <p>
 * 公司附件
 * </p>
 *
 * @author litl
 * @since 2022-10-20
 */
@Data
@EqualsAndHashCode(callSuper=true)
@ToString
public class CompanyAttachment extends AttachmentBaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 公司ID
     */
    private Integer companyId;

}
