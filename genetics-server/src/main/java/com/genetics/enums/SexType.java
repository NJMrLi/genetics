package com.genetics.enums;

public enum SexType {

    MALE(1,"MR", "Male", "男性", "先生"),
    FEMALE(2,"MRS", "Female", "女性", "女士");

    private final Integer id;
    private final String title;      // Mr / Mrs
    private final String english;    // male / female
    private final String chinese;    // 男性 / 女性
    private final String titleCn;

    SexType(Integer id, String title, String english, String chinese, String titleCn) {
        this.id = id;
        this.title = title;
        this.english = english;
        this.chinese = chinese;
        this.titleCn = titleCn;
    }

    /* Getter */
    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getEnglish() {
        return english;
    }

    public String getChinese() {
        return chinese;
    }

    public String getTitleCn() {
        return titleCn;
    }

    public static SexType getById(Integer id) {
        for (SexType sexType : SexType.values()) {
            if (sexType.getId().equals(id)) {
                return sexType;
            }
        }
        throw new IllegalArgumentException("Invalid SexType id: " + id);
    }
}