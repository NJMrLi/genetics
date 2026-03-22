package com.genetics.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * 运营类目枚举
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum OptCategory {

    VAT("01", "VAT服务", ""),
    EPR("02", "EPR服务", ""),

    VAT_SVR("0101", "VAT服务", "01"),

    EPR_PKG("0201", "包装法", "02"),
    EPR_WEEE("0202", "WEEE", "02"),
    EPR_BAT("0203", "电池法", "02"),
    EPR_FUR("0204", "家具法", "02"),
    EPR_TYR("0205", "轮胎法", "02"),
    EPR_PAP("0206", "印刷纸法", "02"),
    EPR_TEX("0207", "纺织品法", "02"),
    EPR_GAR("0208", "化学用品法", "02"),
    EPR_SPO("0209", "刺穿医疗设备法", "02"),

    VAT_SVR_REGDEC("010101", "VAT新注册申报", "0101"),
    VAT_SVR_AGEDEC("010102", "VAT转代理申报", "0101"),
    VAT_SVR_RENEW("010103", "VAT申报续费", "0101"),
    VAT_SVR_DEREG("010104", "VAT注销", "0101"),
    VAT_SVR_SUPDEC("010105", "VAT补申报", "0101"),
    VAT_SVR_CHGDEC("010106", "VAT改申报", "0101"),
    VAT_SVR_ADDON("010107", "VAT增值服务", "0101"),
    VAT_FALLBACK("010199", "VAT兜底", "0101"),

    EPR_PKG_REGDEC("020101", "包装法新注册申报", "0201"),
    EPR_PKG_AGEDEC("020102", "包装法转代理申报", "0201"),
    EPR_PKG_RENEW("020103", "包装法申报续费", "0201"),
    EPR_PKG_DEREG("020104", "包装法注销", "0201"),
    EPR_PKG_ADDCAT("020105", "包装法附加品类", "0201"),
    EPR_PKG_FALLBACK("020199", "包装法兜底", "0201"),

    EPR_WEEE_REGDEC("020201", "WEEE新注册申报", "0202"),
    EPR_WEEE_AGEDEC("020202", "WEEE转代理申报", "0202"),
    EPR_WEEE_RENEW("020203", "WEEE申报续费", "0202"),
    EPR_WEEE_DEREG("020204", "WEEE注销", "0202"),
    EPR_WEEE_ADDCAT("020205", "WEEE附加品类品牌", "0202"),
    EPR_WEEE_ADDBRA("020206", "WEEE附加品牌", "0202"),
    EPR_WEEE_FALLBACK("020299", "WEEE兜底", "0202"),

    EPR_BAT_REGDEC("020301", "电池法新注册申报", "0203"),
    EPR_BAT_AGEDEC("020302", "电池法转代理申报", "0203"),
    EPR_BAT_RENEW("020303", "电池法申报续费", "0203"),
    EPR_BAT_DEREG("020304", "电池法注销", "0203"),
    EPR_BAT_ADDCAT("020305", "电池法附加品牌", "0203"),
    EPR_BAT_ADDBRA("020306", "电池法附加品类", "0203"),
    EPR_BAT_FALLBACK("020399", "电池法兜底", "0203"),

    EPR_FUR_REGDEC("020401", "家具法新注册申报", "0204"),
    EPR_FUR_AGEDEC("020402", "家具法转代理申报", "0204"),
    EPR_FUR_RENEW("020403", "家具法申报续费", "0204"),
    EPR_FUR_DEREG("020404", "家具法注销", "0204"),
    EPR_FUR_FALLBACK("020499", "家具法兜底", "0204"),

    EPR_TYR_REGDEC("020501", "轮胎法新注册申报", "0205"),
    EPR_TYR_AGEDEC("020502", "轮胎法转代理申报", "0205"),
    EPR_TYR_RENEW("020503", "轮胎法申报续费", "0205"),
    EPR_TYR_DEREG("020504", "轮胎法注销", "0205"),
    EPR_TYR_FALLBACK("020599", "轮胎法兜底", "0205"),

    EPR_PAP_REGDEC("020601", "印刷纸新注册申报", "0206"),
    EPR_PAP_AGEDEC("020602", "印刷纸转代理申报", "0206"),
    EPR_PAP_RENEW("020603", "印刷纸申报续费", "0206"),
    EPR_PAP_DEREG("020604", "印刷纸注销", "0206"),
    EPR_PAP_FALLBACK("020699", "印刷纸兜底", "0206"),

    EPR_TEX_REGDEC("020701", "纺织品新注册申报", "0207"),
    EPR_TEX_AGEDEC("020702", "纺织品转代理申报", "0207"),
    EPR_TEX_RENEW("020703", "纺织品申报续费", "0207"),
    EPR_TEX_DEREG("020704", "纺织品注销", "0207"),
    EPR_TEX_FALLBACK("020799", "纺织品兜底", "0207"),

    EPR_GAR_REGDEC("020801", "化学用品新注册申报", "0208"),
    EPR_GAR_AGEDEC("020802", "化学用品转代理申报", "0208"),
    EPR_GAR_RENEW("020803", "化学用品申报续费", "0208"),
    EPR_GAR_DEREG("020804", "化学用品注销", "0208"),
    EPR_GAR_FALLBACK("020804", "化学用品兜底", "0208"),

    EPR_SPO_REGDEC("020901", "刺穿医疗设备新注册申报", "0209"),
    EPR_SPO_AGEDEC("020902", "刺穿医疗设备转代理申报", "0209"),
    EPR_SPO_RENEW("020903", "刺穿医疗设备法申报续费", "0209"),
    EPR_SPO_DEREG("020904", "刺穿医疗设备注销", "0209"),
    EPR_SPO_FALLBACK("020904", "刺穿医疗设备兜底", "0209");

    private final String code;
    private final String name;
    private final String parentCode;

    OptCategory(String code, String name, String parentCode) {
        this.code = code;
        this.name = name;
        this.parentCode = parentCode;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getParentCode() {
        return parentCode;
    }

    public boolean isMatch(String operationCategoryCode) {
        if (operationCategoryCode == null || operationCategoryCode.isEmpty()) {
            return false;
        }
        int codeLength = this.code.length();
        if (operationCategoryCode.length() < codeLength) {
            return false;
        }
        return this.code.equals(operationCategoryCode.substring(0, codeLength));
    }

    public static OptCategory valueOfCode(String code) {
        for (OptCategory type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("OptCategory code: " + code);
    }

    public static Boolean checkIsRenewBusiness(String code) {
        OptCategory optCategory = OptCategory.valueOfCode(code);
        return getRenewOptCategories().contains(optCategory);
    }

    public static List<OptCategory> getRenewOptCategories() {
        return Arrays.asList(VAT_SVR_RENEW, EPR_PKG_RENEW, EPR_WEEE_RENEW, EPR_BAT_RENEW,
                EPR_FUR_RENEW, EPR_TYR_RENEW, EPR_PAP_RENEW, EPR_TEX_RENEW, EPR_GAR_RENEW, EPR_SPO_RENEW);
    }

    public static Boolean checkIsDeregistBusiness(String code) {
        OptCategory optCategory = OptCategory.valueOfCode(code);
        return getDeregistOptCategories().contains(optCategory);
    }

    public static List<OptCategory> getDeregistOptCategories() {
        return Arrays.asList(VAT_SVR_DEREG, EPR_PKG_DEREG, EPR_WEEE_DEREG, EPR_BAT_DEREG,
                EPR_FUR_DEREG, EPR_TYR_DEREG, EPR_PAP_DEREG, EPR_TEX_DEREG, EPR_GAR_DEREG, EPR_SPO_DEREG);
    }

    public static Boolean checkIsEPRBusiness(String code) {
        OptCategory optCategory = OptCategory.valueOfCode(code);
        return optCategory != null && optCategory.getCode().startsWith(EPR.getCode());
    }

    public static Boolean checkIsEPRAddCatBusiness(String code) {
        OptCategory optCategory = OptCategory.valueOfCode(code);
        return Arrays.asList(EPR_BAT_ADDCAT, EPR_BAT_ADDBRA, EPR_PKG_ADDCAT,
                EPR_WEEE_ADDCAT, EPR_WEEE_ADDBRA).contains(optCategory);
    }

    public static Boolean checkIsVatBusiness(String code) {
        OptCategory optCategory = OptCategory.valueOfCode(code);
        return optCategory != null && optCategory.getCode().startsWith(VAT.getCode());
    }

    public static Boolean checkIsEprREGorAGE(String code) {
        OptCategory optCategory = OptCategory.valueOfCode(code);
        return Arrays.asList(VAT_SVR_REGDEC, VAT_SVR_AGEDEC, EPR_PKG_REGDEC, EPR_PKG_AGEDEC,
                EPR_WEEE_REGDEC, EPR_WEEE_AGEDEC, EPR_BAT_REGDEC, EPR_BAT_AGEDEC,
                EPR_FUR_REGDEC, EPR_FUR_AGEDEC, EPR_TYR_REGDEC, EPR_TYR_AGEDEC,
                EPR_PAP_REGDEC, EPR_PAP_AGEDEC, EPR_TEX_REGDEC, EPR_TEX_AGEDEC,
                EPR_GAR_REGDEC, EPR_GAR_AGEDEC, EPR_SPO_REGDEC, EPR_SPO_AGEDEC).contains(optCategory);
    }

    public static Boolean checkIsEprREG(String code) {
        OptCategory optCategory = OptCategory.valueOfCode(code);
        return Arrays.asList(VAT_SVR_REGDEC, EPR_PKG_REGDEC, EPR_WEEE_REGDEC, EPR_BAT_REGDEC,
                EPR_FUR_REGDEC, EPR_TYR_REGDEC, EPR_PAP_REGDEC, EPR_TEX_REGDEC,
                EPR_GAR_REGDEC, EPR_SPO_REGDEC).contains(optCategory);
    }

    public static Boolean checkIsEprAGE(String code) {
        OptCategory optCategory = OptCategory.valueOfCode(code);
        return Arrays.asList(VAT_SVR_AGEDEC, EPR_PKG_AGEDEC, EPR_WEEE_AGEDEC, EPR_BAT_AGEDEC,
                EPR_FUR_AGEDEC, EPR_TYR_AGEDEC, EPR_PAP_AGEDEC, EPR_TEX_AGEDEC,
                EPR_GAR_AGEDEC, EPR_SPO_AGEDEC).contains(optCategory);
    }

    public static Boolean checkNeedEffectiveCompanyTaxNo(String code) {
        OptCategory optCategory = OptCategory.valueOfCode(code);
        return Arrays.asList(VAT_SVR_CHGDEC, VAT_SVR_SUPDEC, VAT_SVR_DEREG,
                EPR_PKG_ADDCAT, EPR_WEEE_ADDCAT, EPR_BAT_ADDCAT,
                EPR_WEEE_ADDBRA, EPR_BAT_ADDBRA,
                EPR_PKG_DEREG, EPR_WEEE_DEREG, EPR_BAT_DEREG, EPR_FUR_DEREG,
                EPR_TYR_DEREG, EPR_PAP_DEREG, EPR_TEX_DEREG, EPR_GAR_DEREG, EPR_SPO_DEREG).contains(optCategory);
    }

    public static List<OptCategory> getRegistryAndAgentOptCategories(String operationCategoryCode2) {
        OptCategory category = OptCategory.valueOfCode(operationCategoryCode2);
        if (category == null) {
            return Arrays.asList();
        }
        switch (category) {
            case EPR_PKG:
                return Arrays.asList(EPR_PKG_REGDEC, EPR_PKG_AGEDEC);
            case EPR_WEEE:
                return Arrays.asList(EPR_WEEE_REGDEC, EPR_WEEE_AGEDEC);
            case EPR_BAT:
                return Arrays.asList(EPR_BAT_REGDEC, EPR_BAT_AGEDEC);
            case EPR_FUR:
                return Arrays.asList(EPR_FUR_REGDEC, EPR_FUR_AGEDEC);
            case EPR_TYR:
                return Arrays.asList(EPR_TYR_REGDEC, EPR_TYR_AGEDEC);
            case EPR_PAP:
                return Arrays.asList(EPR_PAP_REGDEC, EPR_PAP_AGEDEC);
            case EPR_TEX:
                return Arrays.asList(EPR_TEX_REGDEC, EPR_TEX_AGEDEC);
            case EPR_GAR:
                return Arrays.asList(EPR_GAR_REGDEC, EPR_GAR_AGEDEC);
            case EPR_SPO:
                return Arrays.asList(EPR_SPO_REGDEC, EPR_SPO_AGEDEC);
            default:
                return Arrays.asList();
        }
    }

    public static List<String> getRegistryAndAgentOptCodes(String operationCategoryCode2) {
        return getRegistryAndAgentOptCategories(operationCategoryCode2).stream()
                .map(OptCategory::getCode).toList();
    }

    private static final Set<String> FALLBACK_FLOW_CODE3_WHITELIST = Set.of(
            VAT_FALLBACK.getCode(), EPR_BAT_FALLBACK.getCode(), EPR_FUR_FALLBACK.getCode(),
            EPR_TYR_FALLBACK.getCode(), EPR_PAP_FALLBACK.getCode(), EPR_TEX_FALLBACK.getCode(),
            EPR_GAR_FALLBACK.getCode(), EPR_SPO_FALLBACK.getCode());

    public static boolean isConfirmedFlow(String operationCategoryCode3) {
        if (operationCategoryCode3 == null || operationCategoryCode3.isEmpty()) {
            return false;
        }
        return !FALLBACK_FLOW_CODE3_WHITELIST.contains(operationCategoryCode3);
    }
}
