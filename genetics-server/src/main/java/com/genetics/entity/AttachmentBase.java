package com.genetics.entity;

import com.genetics.enums.AttachmentInfoEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString
@EqualsAndHashCode(callSuper = true)
public class AttachmentBase extends FileBase {

    /**
     * 附件类型
     */
    @Schema(description = "附件类型ID")
    private Integer fileTypeId;

    /**
     * 附件类型
     */
    @Schema(description = "附件类型名称")
    private String fileTypeName;

    /**
     * 附件组
     */
    @Schema(description = "附件组ID")
    private Integer fileGroupId;

    /**
     * 附件组
     */
    @Schema(description = "附件组名称")
    private String fileGroupName;

    /**
     * 排序字段
     */
    @Schema(description = "排序字段")
    private Integer sort;

    /**
     * 1生成、2上传
     */
    @Schema(description = "1生成、2上传")
    private Integer sourceType;

    public void defineFileType(AttachmentInfoEnum attachmentInfoEnum) {
        setFileTypeId(attachmentInfoEnum.getFileTypeId());
        setFileTypeName(attachmentInfoEnum.getFileTypeName());
        setFileGroupId(attachmentInfoEnum.getFileGroupId());
        setFileGroupName(attachmentInfoEnum.getFileGroupName());
    }

}


