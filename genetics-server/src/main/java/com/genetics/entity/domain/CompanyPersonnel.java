package com.genetics.entity.domain;

import com.genetics.entity.Entity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * <p>
 * 公司人员信息
 * </p>
 *
 * @author litl
 * @since 2022-10-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString
@Schema(description = "公司人员信息")
public class CompanyPersonnel extends Entity {

    private static final long serialVersionUID = 1L;

    /**
     * 公司id
     */
    @Schema(description = "公司ID")
    private Integer companyId;

    /**
     * 类型ID 1.法人 2.联系人
     */
    @Schema(description = "类型ID 1.法人 2.联系人")
    private Integer typeId;

    /**
     * 类别名称 1.法人2.联系人
     */
    @Schema(description = "类别名称 1.法人2.联系人")
    private String typeName;

    /**
     * 姓名
     */
    @Schema(description = "姓名")
    private String name;

    /**
     * 英文名
     */
    @Schema(description = "英文名")
    private String enname;

    /**
     * 姓名首拼音或英文
     */
    @Schema(description = "姓名首拼音或英文")
    private String firstName;

    /**
     * 姓名剩余拼音或英文
     */
    @Schema(description = "姓名剩余拼音或英文")
    private String secondName;

    /**
     * 证件类型 身份证 护照
     */
    @Schema(description = "证件类型 身份证  护照")
    private String certificatesNoType;

    /**
     * 证件号
     */
    @Schema(description = "证件号")
    private String certificatesNo;

    /**
     * 职位id
     */
    @Schema(description = "职位ID")
    private Integer jobId;

    /**
     * 职位名称
     */
    @Schema(description = "职位名称")
    private String jobName;

    /**
     * 联系电话
     */
    @Schema(description = "联系电话")
    private String telephone;

    /**
     * 联系邮箱
     */
    @Schema(description = "联系邮箱")
    private String email;

    /**
     * 性别 1男2女
     */
    @Schema(description = "性别 1男2女")
    private Integer sex;

    /**
     * 称谓（先生（MR) 女士（MRS））
     */
    @Schema(description = "称谓（先生（MR)  女士（MRS））")
    private String salutation;

    /**
     * 法人英文地址
     */
    @Schema(description = "法人英文地址")
    private String legalPersonEnAddress;

    /**
     * 法人中文地址
     */
    @Schema(description = "法人中文地址")
    private String legalPersonAddress;

    /**
     * 法人生日
     */
    @Schema(description = "法人生日")
    private String legalPersonBirth;

    /**
     * 法人地址邮编
     */
    @Schema(description = "法人地址邮编")
    private String legalPersonZipCode;

}
