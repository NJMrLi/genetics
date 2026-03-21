package com.genetics.common.constant;

import java.util.Arrays;
import java.util.List;

public class RegionCodeConstant {

    public static final String CHINA = "CHN";
    public static final String CHINA_NAME = "中国";
    public static final String CHINA_CURRENCY_CODE = "CNY";
    public static final String CHINA_CURRENCY_NAME = "人民币";
    public static final String TAIWAN = "TWN";
    public static final String HONG_KONG = "HKG";
    public static final String MACAU = "MAC";

    public static final List<String> CHINA_REGIONS = Arrays.asList(RegionCodeConstant.CHINA,
            RegionCodeConstant.TAIWAN,
            RegionCodeConstant.HONG_KONG,
            RegionCodeConstant.MACAU);

    public static final String GERMANY = "DEU";
    public static final Integer GERMANY_ID = 276;
    public static final String GERMANY_SHORT_CODE = "DE";
    public static final String GERMANY_NAME_CN = "德国";
    public static final String GREAT_BRITAIN = "GBR";  // Great Britain
    public static final String GREAT_BRITAIN_NAME_CN = "英国";
    public static final String FRANCE = "FRA";
    public static final String FRANCE_SHORT_CODE = "FR";   // French Republic
    public static final String FRANCE_NAME_CN = "法国";
    public static final String ITALY = "ITA";           // Italian Republic
    public static final String ITALY_NAME_CN = "意大利";
    public static final String SPAIN = "ESP";           // Kingdom of Spain
    public static final String SPAIN_SHORT_CODE = "ES";
    public static final String SPAIN_NAME_CN = "西班牙";
    public static final String POLAND = "POL";          // Republic of Poland
    public static final String POLAND_NAME_CN = "波兰";
    public static final String CZECH_REPUBLIC = "CZE";  // Czech Republic
    public static final String CZECH_REPUBLIC_NAME_CN = "捷克";
    public static final String NETHERLANDS = "NLD";  // Netherlands
    public static final String NETHERLANDS_NAME_CN = "荷兰";
    public static final String IRELAND = "IRL";  //
    public static final String IRELAND_NAME_CN = "爱尔兰";

    public static final String BELGIUM = "BEL";
    public static final String BELGIUM_SHORT_CODE = "BE";
    public static final String BELGIUM_NAME_CN = "比利时";

    public static final String AUSTRIA = "AUT";
    public static final String AUSTRIA_SHORT_CODE = "AT";
    public static final String AUSTRIA_NAME_CN = "奥地利";

    public static final String SWEDEN = "SWE";
    public static final String SWEDEN_SHORT_CODE = "SE";
    public static final String SWEDEN_NAME_CN = "瑞典";

    public static final String SWITZERLAND = "CHE";
    public static final String SWITZERLAND_SHORT_CODE = "CH";
    public static final String SWITZERLAND_NAME_CN = "瑞士";

    private static final List<String> LOCAL_TAX_NUMBER_REQUIRED_COUNTRIES = Arrays.asList(SPAIN,SPAIN_SHORT_CODE, GERMANY,GERMANY_SHORT_CODE, AUSTRIA,AUSTRIA_SHORT_CODE);

    public static boolean isLocalTaxNumberRequired(String regionCode) {
        if (regionCode == null || regionCode.isEmpty()) {
            return false;
        }
        return LOCAL_TAX_NUMBER_REQUIRED_COUNTRIES.contains(regionCode);
    }

    public static List<String> getLocalTaxNumberRequiredCountries() {
        return LOCAL_TAX_NUMBER_REQUIRED_COUNTRIES;
    }


}
