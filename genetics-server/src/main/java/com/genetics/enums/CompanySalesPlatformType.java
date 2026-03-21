package com.genetics.enums;

public enum CompanySalesPlatformType {

    AMAZON(10261, "Amazon"),
    EBAY(10262, "EBay"),
    SHOPIFY(10263, "Shopify"),
    ALLGEGRO(10264, "ALLGEGRO"),
    TIKTOK(10265, "TikTok"),
    CDISCOUNT(10266, "Cdiscount"),
    OTTO(10267, "Otto"),
    TEMU(10268, "Temu"),
    ALIEXPRESS(10269, "AliExpress"),
    OWN_WEBSITE(10270, "OwnWebsite"),
    OFFLINE_SALES(10271, "OfflineSales"),
    OTHER(10272, "Other");

    private final int id;
    private final String name;

    CompanySalesPlatformType(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static CompanySalesPlatformType getById(int id) {
        for (CompanySalesPlatformType type : values()) {
            if (type.id == id) {
                return type;
            }
        }
        return null;
    }

}
