package com.genetics.enums;

public enum CompanyKindType {

    SOLE_TRADER(10128, "2000", "Sole trader", "独资企业"),
    PARTNERSHIP(10129, "2001", "Partnership", "合伙企业"),
    LIMITED_LIABILITY(10130, "2002", "Limited liability company", "有限责任公司"),
    LIMITED_BY_SHARES(10131, "2003", "Limited by shares Company", "股份有限公司"),
    INDIVIDUALLY_OWNED(10192, "2004", "Individually-owned business", "个体工商户");

    private final int id;
    private final String code;
    private final String enName;
    private final String cnName;

    CompanyKindType(int id, String code, String enName, String cnName) {
        this.id = id;
        this.code = code;
        this.enName = enName;
        this.cnName = cnName;
    }

    public int getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getEnName() {
        return enName;
    }

    public String getCnName() {
        return cnName;
    }

    /* 根据中文名取枚举 */
    public static CompanyKindType ofCnName(String cnName) {
        for (CompanyKindType e : values()) {
            if (e.cnName.equals(cnName)) {
                return e;
            }
        }
        return null;
    }

    /* 根据 code 取枚举 */
    public static CompanyKindType ofCode(String code) {
        for (CompanyKindType e : values()) {
            if (e.code.equals(code)) {
                return e;
            }
        }
        return null;
    }

    public static CompanyKindType ofId(Integer id) {
        for (CompanyKindType e : values()) {
            if (e.id == id) {
                return e;
            }
        }
        return null;
    }
}
