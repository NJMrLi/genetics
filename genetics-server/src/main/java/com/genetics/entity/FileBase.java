package com.genetics.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class FileBase {

    /**
     * 文件id
     */
    @Schema(description = "附件id")
    private Integer fileId;
    /**
     * 文件名称
     */
    @Schema(description = "附件名称")
    private String fileName;
    /**
     * 文件后缀
     */
    @Schema(description = "文件后缀")
    private String fileExtension;

}


