package com.genetics.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 附件实体基类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class AttachmentBaseEntity extends Entity {

    private static final long serialVersionUID = 1L;

    /**
     * 附件名称
     */
    private String fileName;

    /**
     * 附件原始名称
     */
    private String originalFileName;

    /**
     * 附件路径
     */
    private String filePath;

    /**
     * 附件URL
     */
    private String fileUrl;

    /**
     * 附件大小(字节)
     */
    private Long fileSize;

    /**
     * 附件类型/MIME类型
     */
    private String fileType;

    /**
     * 附件扩展名
     */
    private String fileExtension;

    /**
     * 附件分类ID
     */
    private Integer attachmentTypeId;

    /**
     * 附件分类名称
     */
    private String attachmentTypeName;

    /**
     * 备注
     */
    private String remark;
}
