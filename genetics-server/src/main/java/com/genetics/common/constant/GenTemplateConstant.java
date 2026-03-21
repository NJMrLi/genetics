package com.genetics.common.constant;

import java.util.ArrayList;
import java.util.List;

public class GenTemplateConstant {

    // region 文件名称
    public static final String BUSINESS_LICENSE_PDF = "BUSINESS-LICENSE.pdf";
    public static final String  ID_CARD_TRANSLATION = " ID-CARD-TRANSLATION.pdf";


    /**
     * 648 fileName
     */
    public static final String FILE_NAME_648 = "Template-of-64-8-Form.pdf";
    /**
     * 授权委托书 fileName
     */
    public static final String TEMPLATE_OF_POA = "Template-of-POA.pdf";
    /**
     * 德国转代理问卷调查表模板名称
     */
    public static final String GER_REGISTER_QUESTION = "ger_register_question.xml";
    /**
     * 德国注册问卷调查表模板名称
     */
    public static final String GER_AGENT_QUESTION = "ger_agent_question.xml";
    /**
     * 德国问卷调查表模板附件名称
     */
    public static final String DE_QUESTIONNAIRE = "DE_Quetionnaire.DOC";
    /**
     * 484模板附件名称
     */
    public static final String TEMPLATE_OF_484 = "Template-of-484-Form.pdf";
    /**
     * 648ftl文件名称
     */
    public static final String FTL_NAME_REGISTER_648 = "vat_register_648";
    public static final String FTL_NAME_AGENT_648 = "vat_agent_648";
    /**
     * 授权委托书ftl文件名称
     */
    public static final String VAT_UK_POA = "vat_uk_poa";
    /**
     * 德国问卷表ftl文件名称
     */
    public static final String GER_POWER_ATTORNEY = "ger_power_attorney";
    /**
     * 邮件同意书
     */
    public static final String GER_EMAIL_CONSENT = "ger_email_consent.pdf";


    /**
     * 荷兰VAT新注册POA
     */
    public static final String NLD_VAT_REGISTER_POA = "netherlands_vat_register_poa.pdf";
    /**
     * 爱尔兰VAT新注册POA
     */
    public static final String IRE_VAT_REGISTER_POA = "ireland_vat_register_poa.pdf";

    /**
     * 爱尔兰VAT转代理POA
     */
    public static final String IRE_VAT_AGENT_POA = "ireland_vat_agent_poa.pdf";

    /**
     * 奥地利VAT新注册/转代理POA
     */
    public static final String AUT_VAT_POA = "austria_vat_poa.pdf";

    /**
     *  瑞士VAT新注册/转代理POA
     */
    public static final String CHE_VAT_POA = "switzerland_vat_poa.pdf";

    /**
     * 捷克活动开始确认函
     */
    public static final String EVENT_START_CONFIRMATION_LETTER = "czech_event_start_confirmation_letter.pdf";

    /**
     * 爱尔兰VAT TR2签字页模板
     */
    public static final String IRE_VAT_TR2_SIGN_PAGE = "ireland_vat_tr2_sign_page.pdf";

    /**
     * 484ftl文件名称
     */
    public static final String VAT_AGENT_484 = "vat_agent_484";
    /**
     * 营业执照ftl文件名称
     */
    public static final String VAT_BUSINESS_LICENSE = "vat_business_license";
    /**
     * 展示身份证ftl文件名称
     */
    public static final String VAT_RESIDENT_ID_CARD = "vat_resident_id_card";
    /**
     * 公司信用报告
     */
    public static final String COMPANY_CREDIT_REPORT = "vat_company_credit_report";

    public static final String VAT_TRIO_CZE = "vat_cze_trio.xlsx";
    public static final String VAT_TRIO_POL = "vat_pol_trio.xlsx";
    public static final String VAT_TRIO_FRA = "vat_fra_trio.xlsx";

    /**
     * 法国POA文件
     */
    public static final String VAT_FR_POA_EU_MALE = "vat_fr_poa_eu_male.pdf";
    public static final String VAT_FR_POA_EU_FEMALE = "vat_fr_poa_eu_female.pdf";
    public static final String VAT_FR_POA_NON_EU_MALE = "vat_fr_poa_non_eu_male.pdf";
    public static final String VAT_FR_POA_NON_EU_FEMALE = "vat_fr_poa_non_eu_female.pdf";

    /**
     * 法国VAT注销文件
     */
    public static final String VAT_FRA_TERMINATION_ATG_MALE = "vat_fra_termination_atg_male.pdf";
    public static final String VAT_FRA_TERMINATION_ATG_FEMALE = "vat_fra_termination_atg_female.pdf";
    public static final String VAT_FRA_TERMINATION_EGC_MALE = "vat_fra_termination_egc_male.pdf";
    public static final String VAT_FRA_TERMINATION_EGC_FEMALE = "vat_fra_termination_egc_female.pdf";

    /**
     * 捷克POA
     */
    public static final String VAT_CZ_POA = "vat_cz_poa";
    /**
     * 波兰注销申请书
     */
    public static final String VAT_POL_DEREGISTRATION_LETTER_TEMP = "vat_pol_deregistration_letter.pdf";
    /**
     * 法国注销申请书 文件名字不好， 没办法改了
     */
    public static final String VAT_DEREGISTRATION_LETTER_TEMP = "template-of-termaination-letter.pdf";
    public static final String VAT_FRA_DEREGISTRATION_LETTER_EGC_MALE = "vat_fra_termination_letter_egc_male";
    public static final String VAT_FRA_DEREGISTRATION_LETTER_ATG_MALE = "vat_fra_termination_letter_atg_male";
    public static final String VAT_FRA_DEREGISTRATION_LETTER_EGC_FEMALE = "vat_fra_termination_letter_egc_female";
    public static final String VAT_FRA_DEREGISTRATION_LETTER_ATG_FEMALE = "vat_fra_termination_letter_atg_female";

    /**
     * 法国单方面解约函
     */
    public static final String VAT_FRA_UNILATERAL_CANCELLATION_LETTER = "vat_fra_termination_letter.pdf";



    // endregion

    // region  模板HTML地址
    /**
     * 营业执照
     */
    public static final String BUSINESS_LICENSE = "%s%s%d/sherry/api/vat-server-ticket/business-license?serverId=%s&showBodyType=%d&access_token=%s";
    /**
     * 身份证
     */
    public static final String RESIDENT_ID_CARD = "%s%s%d/sherry/api/vat-server-ticket/resident-id-card?serverId=%s&showBodyType=%d&access_token=%s";
    /**
     * 信用证明
     */
    public static final String CREDIT_REPORT = "%s%s%d/sherry/api/vat-server-ticket/company-credit-report?serverId=%s&showBodyType=%d&access_token=%s";
    /**
     * 德国授权委托书
     */
    public static final String SERVER_POWER_ATTORNEY = "%s%s%d/sherry/api/vat-server-power-attorney?id=%d&access_token=%s";
    /**
     * 法国授权委托书
     */
    public static final String FRA_SERVER_POWER_ATTORNEY = "%s%s%d/sherry/api/vat-server-fr-poa?id=%d&access_token=%s";
    /**
     * 捷克授权委托书
     */
    public static final String CZ_SERVER_POWER_ATTORNEY = "%s%s%d/sherry/api/vat-server-cz-poa?id=%d&access_token=%s";
    /**
     * 电子邮件同意书
     */
    public static final String SERVER_EMAIL_CONSENT = "%s%s%d/sherry/api/vat-server-email-consent?id=%d&access_token=%s";
    /**
     * 英国HMRC_64_8_模版
     */
    public static final String SERVER_648 = "%s%s%d/sherry/api/vat-server-648?id=%d&access_token=%s";
    /**
     * 484文件
     */
    public static final String SERVER_484 = "%s%s%d/sherry/api/vat-server-484?id=%d&access_token=%s";
    /**
     * 英国授权委托书
     */
    public static final String SERVER_AUTH = "%s%s%d/sherry/api/vat-server-auth?id=%d&access_token=%s";

    /**
     * 法国VAT注销模板
     */
    public static final String FRA_VAT_DEREGISTRATION_LETTER = "%s%s%d/sherry/api/vat-server-fra-termination-letter?id=%d&access_token=%s";

    // endregion

    /**
     * 注册提交资料生成模版标记位，0代表不生成模版，1代表生成模版
     */
    public final static Integer REGISTER_GENERATE_TEMPLATE = 1;


    /**
     * 历史中国卖家
     */
    public final static List<String> ATG_HISTORY_VRN_LIST= new ArrayList<>();

    static {
        ATG_HISTORY_VRN_LIST.add("1234567890");
    }

}
