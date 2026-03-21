package com.genetics.enums;

public enum BusinessCodeStatus {

    NO_NEED(1, "不需要"),
    NEED_UNUPLOAD(2, "需要未上传"),
    NEED_UPLOADED(3, "需要已上传"),
    ;

    private final Integer id;
    private final String name;

    BusinessCodeStatus(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }


    /**
     * 根据中文名称返回枚举，找不到返回 null
     */
    public static BusinessCodeStatus getTypeByName(String name) {
        for (BusinessCodeStatus t : values()) {
            if (t.name.equals(name)) {
                return t;
            }
        }
        return null;
    }

    public static BusinessCodeStatus getTypeByid(Integer id) {
        for (BusinessCodeStatus t : values()) {
            if (t.getId().equals(id)) {
                return t;
            }
        }
        return null;
    }

}
