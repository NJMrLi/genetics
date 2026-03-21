package com.genetics.common.constant;

import com.genetics.entity.Entity;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * 系统内常量配置
 */
@Component
public class SystemConstant {
    public static final String EMPTY = Strings.EMPTY;
    public static final String ALL = "全部";
    public static final Integer ALL_ID = 0;
    public static final String SUPPLY_DOCS = "补充资料";
    public static final String SYSTEM_DEFAULT_USER_NAME = "system";
    public static final Integer SYSTEM_DEFAULT_USER_ID = 1;

    public static final String YSE = "是";
    public static final String NO = "否";

    private static String bdaDepartmentCode;
    private static String itcDepartmentCode;

    @Value("${sherry.bdaDepartmentCode:14.01.03}")
    public void setBdaDepartmentCode(String code) {
        bdaDepartmentCode = code;
    }

    @Value("${sherry.itcDepartmentCode:14.02.01}")
    public void setItcDepartmentCode(String code) {
        itcDepartmentCode = code;
    }

    // 提供静态方法获取配置值
    public static String getBdaDepartmentCode() {
        return bdaDepartmentCode;
    }

    public static String getItcDepartmentCode() {
        return itcDepartmentCode;
    }

    // 扩展方法：校验文件类型白名单
    public static final List<String> ALLOWED_EXTENSIONS = Arrays.asList("pdf", "jpeg", "jpg", "png", "doc", "docx",
            "xls", "xlsx", "csv");

    public static final Integer ONEDAY = 1;
    public static final Integer ONEYEAR = 1;
    public static final Integer FIRSTDAYOFMONTH = 1;

    public static final Integer QUARTER_NUMS_OF_YEAR = 4;
    public static final Integer MONTH_NUMS_OF_QUARTER = 3;
    public static final Integer MONTH_NUMS_OF_YEAR = 12;

    public static void setCreateAndUpdateInfoBySystemDefaultUser(Entity entity, String businessName) {
        entity.setCreatedById(Long.valueOf(SYSTEM_DEFAULT_USER_ID));
        entity.setCreatedByName(SYSTEM_DEFAULT_USER_NAME + ":" + businessName);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setLastUpdatedById(Long.valueOf(SYSTEM_DEFAULT_USER_ID));
        entity.setLastUpdatedByName(SYSTEM_DEFAULT_USER_NAME + ":" + businessName);
        entity.setLastUpdatedAt(LocalDateTime.now());
        entity.setDeleted(0);
    }

    public static void setCreateAndUpdateInfoBySpecialUser(Entity entity, Integer userId, String userName) {
        LocalDateTime now = LocalDateTime.now();
        entity.setCreatedById(Long.valueOf(userId));
        entity.setCreatedByName(userName);
        entity.setCreatedAt(now);
        entity.setLastUpdatedById(Long.valueOf(userId));
        entity.setLastUpdatedByName(userName);
        entity.setLastUpdatedAt(now);
        entity.setDeleted(0);
    }

    // ============== 附件类型ID常量 ==============
    // 身份证件
    public static final Integer ATTACHMENT_IDENTITY_CARD_FRONT = 10001;
    public static final Integer ATTACHMENT_IDENTITY_CARD_BACKEND = 10002;
    public static final Integer ATTACHMENT_UTILITY_BILL = 10003;
    public static final Integer ATTACHMENT_UTILITY_BILL_2 = 10004;

    // 营业执照/注册证书
    public static final Integer ATTACHMENT_BUSINESS_LICENSE = 10005;
    public static final Integer ATTACHMENT_OVERSEAS_REGISTRATION_CERTIFICATE = 10006;

    // VAT相关附件
    public static final Integer ATTACHMENT_VAT_ARTICLES_OF_ASSOCIATION = 10007;
    public static final Integer ATTACHMENT_SCREENSHOT_OF_COMPANY_BANK_ACCOUNT = 10008;
    public static final Integer ATTACHMENT_VAT_ENTERPRISE_CREDIT_REPORT = 10009;
    public static final Integer ATTACHMENT_VAT_TAX_CERTIFICATE = 10010;
    public static final Integer ATTACHMENT_TAX_IDENTIFICATION_NUMBER_CERTIFICATE_VAT = 10011;
    public static final Integer ATTACHMENT_VAT_NUMBER_CERTIFICATE_REGISTRATION = 10012;
    public static final Integer ATTACHMENT_TAX_APOSTILLE = 10013;
    public static final Integer ATTACHMENT_VAT_EORI_EN = 10014;
    public static final Integer ATTACHMENT_VAT_EORI_EU = 10015;
    public static final Integer ATTACHMENT_VAT_EORI_FR_OTHER = 10016;

    // 授权相关
    public static final Integer ATTACHMENT_LETTER_OF_AUTHORIZATION = 10017;
    public static final Integer ATTACHMENT_LETTER_OF_AUTHORIZATION_SERVER_TEMPLATE = 10018;

    // EPR相关附件
    public static final Integer ATTACHMENT_EPR_REG_NUM_CERT = 10019;
    public static final Integer ATTACHMENT_EPR_OTHER_SUPPLEMENTARY = 10020;
    public static final Integer ATTACHMENT_EPR_HISTORY_DECLARATION = 10021;
    public static final Integer ATTACHMENT_EPR_LATEST_PAYMENT_RECORD = 10022;
    public static final Integer ATTACHMENT_EPR_LETTER_OF_CANCELLATION = 10023;
    public static final Integer ATTACHMENT_DEREGISTRATION_LETTER = 10024;
    public static final Integer ATTACHMENT_EPR_TERMINATION_LETTER = 10025;

    // VAT生成附件
    public static final Integer ATTACHMENT_E_MAIL_CONSENT_FORM = 10026;
    public static final Integer ATTACHMENT_DEU_COMPANY_OPERATIONS_LOCATION_QUESTIONNAIRE = 10027;
    public static final Integer ATTACHMENT_FURTHER_INFORMATION = 10028;
    public static final Integer ATTACHMENT_DEU_REGISTRATION_QUESTIONNAIRE = 10029;
    public static final Integer ATTACHMENT_UK64_8 = 10030;
    public static final Integer ATTACHMENT_IRE_TR2_FROM_SIGN_PAGE = 10031;

    // 店铺相关附件
    public static final Integer ATTACHMENT_SALES_ORDER_CONTRACT = 10032;
    public static final Integer ATTACHMENT_INVOICE_FOR_SALES = 10033;
    public static final Integer ATTACHMENT_BANK_RECEIPT = 10034;
    public static final Integer ATTACHMENT_SCREENSHOT_HOMEPAGE_STORE = 10035;
    public static final Integer ATTACHMENT_SCREENSHOT_LOGISTICS_REGISTER = 10036;
    public static final Integer ATTACHMENT_SALES_CERTIFICATE = 10037;
    public static final Integer ATTACHMENT_SCREENSHOT_SELLER_ID = 10038;
    public static final Integer ATTACHMENT_SCREENSHOT_SELLER_CENTER = 10039;
    public static final Integer ATTACHMENT_SCREENSHOT_LOGISTICS = 10040;
    public static final Integer ATTACHMENT_SCREENSHOT_DELIVER_SYS = 10041;
    public static final Integer ATTACHMENT_SCREENSHOT_STORE = 10042;
    public static final Integer ATTACHMENT_LOCAL_WAREHOUSE_LEASE_AGREEMENT = 10043;
    public static final Integer ATTACHMENT_INTERNATIONAL_SALES_CERTIFICATE = 10044;
    public static final Integer ATTACHMENT_EBAY_STORE_CONFIRMATION_EMAIL = 10045;

    /**
     * 同步公司基础信息附件列表
     */
    public static final List<Integer> COMPANY_BASE_ATTACHMENT_FIELDS = Arrays.asList(
            ATTACHMENT_IDENTITY_CARD_FRONT,
            ATTACHMENT_IDENTITY_CARD_BACKEND,
            ATTACHMENT_UTILITY_BILL,
            ATTACHMENT_UTILITY_BILL_2,
            ATTACHMENT_BUSINESS_LICENSE,
            ATTACHMENT_OVERSEAS_REGISTRATION_CERTIFICATE,
            ATTACHMENT_VAT_ARTICLES_OF_ASSOCIATION,
            ATTACHMENT_SCREENSHOT_OF_COMPANY_BANK_ACCOUNT,
            ATTACHMENT_VAT_ENTERPRISE_CREDIT_REPORT,
            ATTACHMENT_VAT_TAX_CERTIFICATE,
            ATTACHMENT_TAX_IDENTIFICATION_NUMBER_CERTIFICATE_VAT,
            ATTACHMENT_VAT_NUMBER_CERTIFICATE_REGISTRATION,
            ATTACHMENT_TAX_APOSTILLE,
            ATTACHMENT_VAT_EORI_EN,
            ATTACHMENT_VAT_EORI_EU,
            ATTACHMENT_VAT_EORI_FR_OTHER
    );

    /**
     * 同步公司基础信息附件列表(EPR)
     */
    public static final List<Integer> EPR_COMPANY_BASE_ATTACHMENT_FIELDS = Arrays.asList(
            ATTACHMENT_IDENTITY_CARD_FRONT,
            ATTACHMENT_IDENTITY_CARD_BACKEND,
            ATTACHMENT_BUSINESS_LICENSE,
            ATTACHMENT_OVERSEAS_REGISTRATION_CERTIFICATE,
            ATTACHMENT_VAT_NUMBER_CERTIFICATE_REGISTRATION,
            ATTACHMENT_LETTER_OF_AUTHORIZATION,
            ATTACHMENT_LETTER_OF_AUTHORIZATION_SERVER_TEMPLATE,
            ATTACHMENT_EPR_REG_NUM_CERT,
            ATTACHMENT_EPR_OTHER_SUPPLEMENTARY,
            ATTACHMENT_EPR_HISTORY_DECLARATION,
            ATTACHMENT_EPR_LATEST_PAYMENT_RECORD,
            ATTACHMENT_EPR_LETTER_OF_CANCELLATION,
            ATTACHMENT_DEREGISTRATION_LETTER
    );

    /**
     * 同步公司税号信息附件列表
     */
    public static final List<Integer> COMPANY_VAT_ATTACHMENT_FIELDS = Arrays.asList(
            ATTACHMENT_VAT_TAX_CERTIFICATE,
            ATTACHMENT_TAX_IDENTIFICATION_NUMBER_CERTIFICATE_VAT,
            ATTACHMENT_TAX_APOSTILLE
    );

    /**
     * 同步公司税号信息附件列表(EPR)
     */
    public static final List<Integer> COMPANY_EPR_ATTACHMENT_FIELDS = Arrays.asList(
            ATTACHMENT_EPR_REG_NUM_CERT,
            ATTACHMENT_EPR_TERMINATION_LETTER,
            ATTACHMENT_EPR_LETTER_OF_CANCELLATION,
            ATTACHMENT_DEREGISTRATION_LETTER
    );

    /**
     * VAT 业务 用户生成的附件
     */
    public static final List<Integer> VAT_GENERATE_ATTACHMENT_FIELDS = Arrays.asList(
            ATTACHMENT_LETTER_OF_AUTHORIZATION,
            ATTACHMENT_E_MAIL_CONSENT_FORM,
            ATTACHMENT_DEU_COMPANY_OPERATIONS_LOCATION_QUESTIONNAIRE,
            ATTACHMENT_FURTHER_INFORMATION,
            ATTACHMENT_DEU_REGISTRATION_QUESTIONNAIRE,
            ATTACHMENT_UK64_8,
            ATTACHMENT_IRE_TR2_FROM_SIGN_PAGE
    );

    /**
     * 同步公司店铺附件列表
     */
    public static final List<Integer> COMPANY_SHOP_ATTACHMENT_FIELDS = Arrays.asList(
            ATTACHMENT_SALES_ORDER_CONTRACT,
            ATTACHMENT_INVOICE_FOR_SALES,
            ATTACHMENT_BANK_RECEIPT,
            ATTACHMENT_SCREENSHOT_HOMEPAGE_STORE,
            ATTACHMENT_SCREENSHOT_LOGISTICS_REGISTER,
            ATTACHMENT_SALES_CERTIFICATE,
            ATTACHMENT_SCREENSHOT_SELLER_ID,
            ATTACHMENT_SCREENSHOT_SELLER_CENTER,
            ATTACHMENT_SCREENSHOT_LOGISTICS,
            ATTACHMENT_SCREENSHOT_DELIVER_SYS,
            ATTACHMENT_SCREENSHOT_STORE,
            ATTACHMENT_LOCAL_WAREHOUSE_LEASE_AGREEMENT,
            ATTACHMENT_INTERNATIONAL_SALES_CERTIFICATE,
            ATTACHMENT_EBAY_STORE_CONFIRMATION_EMAIL
    );

}
