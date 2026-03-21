package com.genetics.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum AttachmentInfoEnum {

    DEU_DECLARATION_RECEIPT_FOR_THE_PAST_YEAR("VAT信息", 3000032, "德国近一年申报回执", 3000001),
    DEU_LETTER_FROM_THE_TAX_OFFICE("VAT信息", 3000033, "德国税局信件", 3000001),
    LETTER_OF_CANCELLATION("VAT信息", 3000034, "与前税务代理的解约函", 3000001),
    TAX_DECLARATION_AND_PAYMENT_VOUCHER("VAT信息", 3000035, "最近一个周期申报税金缴纳凭证", 3000001), // 弃用，改用3000115
    TAX_APOSTILLE("VAT信息", 3000036, "海牙认证材料", 3000001),
    DEU_TRANSFER_AGENT_QUESTIONNAIRE_TEMPLATE("补充信息_服务端模版", 3000048, "德国转代理问卷表模版", 3000040), // 新增
    UK64_8_TEMPLATE("补充信息_服务端模版", 3000046, "64-8文件模版", 3000040), // 新增
    //PROOF_OF_SUBMISSION("审核信息", 3000053, "提交证明", 3000050), // 新增（fileTypeId 不同）
    AUDIT_ATTACHMENT("审核信息", 3000057, "审核附件", 3000050), // 新增（fileTypeId 不同）
    UK_484_TEMPLATE("补充信息_服务端模版", 3000058, "484文件模版", 3000040),
    LETTER_OF_CANCELLATION_TEMPLATE("VAT信息", 3000059, "与前税务代理的单向解约函模板", 3000001),

    // VAT申报相关（30001xx）
    PROOF_OF_SUBMISSION("审核信息", 3000134, "审核附件", 3000130),
    SERVICE_TICKET("服务单", 3000131, "服务单", 3000130),
    SERVICE_TICKET_TEMPLATE("服务单", 3000132, "服务单模版", 3000130),
    TAX_BUREAU_PROOF_ATT("税局证明", 3000133, "税局证明附件", 3000130),
    VAT_DECLARATION_TERMINATED_FILE("VAT申报作废附件", 3000135, "VAT申报作废附件", 3000130),

    PVA("申报资料", 3000113, "PVA文件", 3000102),
    C79("申报资料", 3000114, "C79文件", 3000102),
    DUTY_PAID_PROOF("申报资料", 3000115, "完税凭证", 3000102),
    RETURN_OF_DECLARATION("申报资料", 3000116, "申报回执", 3000102),
    VAT_DECLARATION_CONFIRMATION("申报确认书", 3000117, "VAT申报确认书", 3000102),

    // 补充信息（30002xx-30003xx）
    FURTHER_INFORMATION_ATT("补充资料", 3000231, "说明附件", 3000230),
    FURTHER_INFORMATION_MAT("补充资料", 3000232, "资料上传", 3000230),


    SOURCE_PURCHASE_CONTRACT("销售信息", 3000325, "货源采购合同", 3000303),
    PROOF_OF_PAYMENT_FOR_SOURCE_PURCHASE("销售信息", 3000326, "货源采购付款证明", 3000303),
    CONTRACT_OF_CARRIAGE_OF_GOODS("销售信息", 3000327, "货品海外运输合同", 3000303),
    PROOF_OF_PAYMENT_FOR_SHIPMENT_OF_GOODS("销售信息", 3000328, "货品海外运输付款证明", 3000303),

    FURTHER_INFORMATION("补充信息", 3000331, "补充资料", 3000305),
    UK64_8("补充信息", 3000333, "64-8文件", 3000305),
    //LETTER_OF_AUTHORIZATION("补充信息", 3000334, "授权委托书（英国）", 3000305), // 新增
    DEU_REGISTRATION_QUESTIONNAIRE("补充信息", 3000335, "德国问卷表", 3000305),
    E_MAIL_CONSENT_FORM("补充信息", 3000336, "电子邮件同意书", 3000305),
    DEU_COMPANY_OPERATIONS_LOCATION_QUESTIONNAIRE("补充信息", 3000337, "公司运营所在地问卷表", 3000305),
    UK64_8_SERVER_TEMPLATE("补充信息_服务端模版", 3000352, "64-8文件模版", 3000305),
    //LETTER_OF_AUTHORIZATION_SERVER_TEMPLATE("补充信息_服务端模版", 3000353, "授权委托书（英国）模版", 3000305), // 新增
    DEU_REGISTRATION_QUESTIONNAIRE_TEMPLATE("补充信息_服务端模版", 3000354, "德国问卷模版", 3000305),
    E_MAIL_CONSENT_FORM_TEMPLATE("补充信息_服务端模版", 3000355, "电子邮件同意书模版", 3000305),
    DEU_COMPANY_OPERATIONS_LOCATION_QUESTIONNAIRE_TEMPLATE("补充信息_服务端模版", 3000356, "公司运营所在地问卷表模版", 3000305),
    TAX_IDENTIFICATION_NUMBER_CERTIFICATE_VAT("VAT信息", 3000359, "本土税号证书", 3000306),
    LETTER_OF_AUTHORIZATION("补充信息", 3000361, "授权委托书", 3000305),
    LETTER_OF_AUTHORIZATION_SERVER_TEMPLATE("补充信息_服务端模版", 3000362, "授权委托书模版", 3000305),
    TRIO_EXCEL_TEMPLATE("补充信息_服务端模版", 3000368, "翠欧服务单模板", 3000308),
    IRE_TR2_FROM_SIGN_PAGE("补充信息", 3000369, "TR2表格签字页", 3000308),
    IRE_TR2_FROM_SIGN_PAGE_TEMPLATE("补充信息", 3000370, "TR2表格签字页模板", 3000308),
    EVENT_START_CONFIRMATION_LETTER_TEMPLATE("补充信息", 3000371, "捷克活动开始确认函模板", 3000308),
    EVENT_START_CONFIRMATION_LETTER("补充信息", 3000372, "捷克活动开始确认函", 3000308),
    AUT_VERF26("补充信息", 3000373, "奥地利Verf26", 3000308),
    AUT_VERF19E("补充信息", 3000374, "奥地利Verf19E", 3000308),
    // 法人信息（30006xx）
    IDENTITY_CARD_FRONT("法人信息", 3000611, "中国居民身份证正面", 3000601),
    IDENTITY_CARD_BACKEND("法人信息", 3000612, "中国居民身份证反面", 3000601),
    IDENTITY_CARD_EN_0("法人信息", 3000613, "中国居民身份证（翻译件）", 3000601),
    PASSPORT_0("法人信息", 3000615, "护照", 3000601),
    UTILITY_BILL("法人信息", 3000617, "法人地址证明一", 3000601),
    LEASING_AGREEMENT("法人信息", 3000618, "租赁协议", 3000601), // 新增
    WORKING_PERMIT("法人信息", 3000619, "工作证许可", 3000601), // 新增
    VISA("法人信息", 3000620, "签证", 3000601), // 新增
    PASSPORT_FIRST_PAGE("法人信息", 3000621, "护照首页", 3000601), // 新增
    LEASING_AGREEMENT_1("法人信息", 3000622, "租赁协议", 3000601), // 新增
    CREDIT_CARD_STATEMENT("法人信息", 3000623, "信用卡账单", 3000601), // 新增
    PROFILE_PHOTO("法人信息", 3000624, "头像", 3000601),
    UTILITY_BILL_2("法人信息", 3000635, "法人地址证明二", 3000601),

    // 公司信息（30006xx）
    BUSINESS_LICENSE("公司信息", 3000625, "大陆公司营业执照", 3000602),
    BUSINESS_LICENSE_EN("公司信息", 3000626, "大陆公司营业执照（翻译件）", 3000602),
    OVERSEAS_REGISTRATION_CERTIFICATE("公司信息", 3000627, "港澳台及海外公司注册登记证", 3000602),
    CREDENTIALS("公司信息", 3000628, "凭证", 3000602),
    ENTERPRISE_CREDIT_REPORT("公司信息", 3000629, "企业信用信息公示报告", 3000602),
    COMPANY_BANK_STATEMENT("公司信息", 3000630, "公司银行对账单(银行证明信)", 3000602),
    CHINESE_TAX_RESIDENT_IDENTITY_CERTIFICATE("公司信息", 3000631, "中国税收居民身份证明", 3000602),
    SCREENSHOT_OF_COMPANY_BANK_ACCOUNT("公司信息", 3000632, "公司银行账户截图", 3000602),
    ENTERPRISE_CREDIT_REPORT_EN("公司信息", 3000633, "企业信用信息公示报告(翻译件)", 3000602),
    SEPA("公司信息", 3000634, "单一欧元支付区", 3000602),


    // 店铺信息（30020xx）
    SALES_ORDER_CONTRACT("店铺信息", 3002030, "销售订单/合同", 3000302),
    INVOICE_FOR_SALES("店铺信息", 3002031, "销售发票", 3000302),
    BANK_RECEIPT("店铺信息", 3002032, "银行收款凭证", 3000302),
    SCREENSHOT_HOMEPAGE_STORE("店铺信息", 3002033, "亚马逊截图-店铺首页", 3000302),
    SCREENSHOT_LOGISTICS_REGISTER("店铺信息", 3002034, "亚马逊截图-FBA注册", 3000302),
    SALES_CERTIFICATE("店铺信息", 3002035, "亚马逊销售证明", 3000302),
    SCREENSHOT_SELLER_ID("店铺信息", 3000318, "亚马逊截图-卖家ID", 3000302),
    SCREENSHOT_SELLER_CENTER("店铺信息", 3000319, "亚马逊截图-卖家中心", 3000302),
    SCREENSHOT_LOGISTICS("店铺信息", 3000320, "亚马逊截图-FBA设置", 3000302),
    SCREENSHOT_DELIVER_SYS("店铺信息", 3000321, "亚马逊截图-准备发货", 3000302),
    SCREENSHOT_STORE("店铺信息", 3000317, "店铺截图", 3000302),
    LOCAL_WAREHOUSE_LEASE_AGREEMENT("店铺信息", 3000324, "当地仓库租赁协议", 3000302),
    INTERNATIONAL_SALES_CERTIFICATE("店铺信息", 3002036, "跨国销售证明", 3000302),
    EBAY_STORE_CONFIRMATION_EMAIL("店铺信息", 3000329, "EBAY开店确认邮件", 3000302),

    // VAT信息（3010xxx）
    VAT_NUMBER_CERTIFICATE_REGISTRATION("公司信息", 3010001, "注册国VAT税号证书", 3010010),
    VAT_ARTICLES_OF_ASSOCIATION("公司信息", 3010002, "公司章程", 3010010),
    VAT_ENTERPRISE_CREDIT_REPORT("公司信息", 3010003, "企业信用报告", 3010010),
    VAT_TAX_CERTIFICATE("VAT信息", 3010004, "税号证书", 3010010),
    VAT_EORI_EN("VAT信息", 3010005, "英国EORI证书", 3010010),
    VAT_EORI_EU("VAT信息", 3002613, "欧盟EORI证书", 3010010),
    VAT_EORI_FR_OTHER("VAT信息", 3002614, "法国EORI其他附件", 3010010),
    VAT_OTHER("VAT信息", 3010006, "其他税号附件", 3010010),

    // 意见反馈（30026xx）
    FEEDBACK_IMAGE("意见反馈", 3002611, "意见反馈图片", 3002601),
    DISPOSE_IMAGE("意见反馈", 3002612, "意见反馈处理附件", 3002601),

    // 其他补充材料
    FURTHER_INFORMATION_INIT("其他", 3000421, "发起补充材料", 3000402),
    FURTHER_INFORMATION_FILLIN("其他", 3000416, "补充材料", 3000402),

    /**
     * 英国 最近一个周期申报回执
     */
    UK_RETURNS_FOR_THE_LATEST_CYCLE("VAT信息", 3000029, "最近一个周期申报回执", 3000001),// 新增

    /**
     * VAT注销申请
     */
    VAT_DEREGISTRATION_LETTER_TEMPLATE("VAT信息", 3000030, "VAT注销申请模板文件", 3000001),// 新增
    VAT_DEREGISTRATION_LETTER("VAT信息", 3000031, "VAT注销申请", 3000001),// 新增

    /**
     * 注销凭证
     */
    DEREGISTRATION_LETTER("注销信息", 3000888, "注销凭证", 3000401),// 新增

    /**
     * EPR注册号证书
     */
    EPR_REG_NUM_CERT("EPR信息", 3000412, "EPR注册号证书", 3000002),
    EPR_OTHER_SUPPLEMENTARY("EPR其他材料", 3000415, "补充材料", 3000402),
    EPR_TRACKING_DATA("EPR其他材料", 3000422, "'追溯数据'", 3000402),
    EPR_HISTORY_DECLARATION("EPR信息", 3000423, "EPR历史申报记录", 3000402),
    EPR_LATEST_PAYMENT_RECORD("EPR信息", 3000424, "EPR最近一次缴费凭证", 3000402),
    EPR_TERMINATION_LETTER("EPR信息", 3000425, "EPR注销文件", 3000402),
    EPR_LETTER_OF_CANCELLATION("EPR信息", 3000426, "EPR解约文件", 3000402),
    EPR_TERMINATION_LETTER_TEMPLATE("EPR信息", 3000427, "EPR注销文件模板", 3000402),
    EPR_LETTER_OF_CANCELLATION_TEMPLATE("EPR信息", 3000428, "EPR解约文件模板", 3000402),
    EPR_FRA_COMMON_POA_TEMPLATE("EPR信息", 3000429, "法国通用POA授权文件模板", 3000402),
    EPR_FRA_COMMON_POA("EPR信息", 3000430, "法国通用POA授权文件", 3000402),


    EPR_ONLINE_PAYMENT_COST_VOUCHER("EPR账单附件", 3000501, "EPR账单明细", 3000500),
    EPR_ONLINE_PAYMENT_OFFLINE_ATTACHMENT("EPR账单附件", 3000502, "EPR账单线下缴费凭证", 3000500),
    EPR_ONLINE_PAYMENT_CONFIRM_ATTACHMENT("EPR账单附件", 3000503, "EPR账单缴费确认附件", 3000500),

    EPR_RETURN_OF_DECLARATION("EPR申报资料", 3000601, "EPR申报凭证", 3000600),

    EPR_DECLARATION_TERMINATED_FILE("EPR申报资料", 3000602, "EPR申报作废凭证", 3000600),


    ;

    /**
     * 构造
     *
     * @param fileGroupName
     * @param fileTypeId
     * @param fileTypeName
     * @param fileGroupId
     */
    AttachmentInfoEnum(String fileGroupName, Integer fileTypeId, String fileTypeName, Integer fileGroupId) {
        this.fileGroupName = fileGroupName;
        this.fileTypeId = fileTypeId;
        this.fileTypeName = fileTypeName;
        this.fileGroupId = fileGroupId;
    }

    private final String fileGroupName;
    private final Integer fileTypeId;
    private final String fileTypeName;
    private final Integer fileGroupId;


    /**
     * 根据FileTypeId获取文件枚举
     *
     * @param fileTypeId
     * @return
     */
    public static AttachmentInfoEnum getAttachmentInfoEnum(Integer fileTypeId) {
        if (fileTypeId == null) {
            return null;
        }
        for (AttachmentInfoEnum enumValue : AttachmentInfoEnum.values()) {
            if (enumValue.getFileTypeId().equals(fileTypeId)) {
                return enumValue;
            }
        }
        return null;
    }
}
