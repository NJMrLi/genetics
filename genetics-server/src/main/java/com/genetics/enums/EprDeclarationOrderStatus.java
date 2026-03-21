package com.genetics.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum EprDeclarationOrderStatus {

    WAIT_SUBMIT(10, "待提交"),

    DECLARATION_IN_PROGRESS(20, "申报中"),

    COMPLETED(50, "已完成"),

    TERMINATED(99, "已作废");

    @EnumValue
    private final Integer id;
    private final String name;

    /**
     * 在途状态集合：待提交、申报中
     */
    public static final EnumSet<EprDeclarationOrderStatus> IN_PROGRESS_STATUSES = EnumSet.of(
            WAIT_SUBMIT,
            DECLARATION_IN_PROGRESS
    );

    EprDeclarationOrderStatus(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public static EnumSet<EprDeclarationOrderStatus> toEnumSet() {
        return EnumSet.allOf(EprDeclarationOrderStatus.class);
    }

    /**
     * 获取在途状态的ID列表
     */
    public static List<Integer> getInProgressStatusIds() {
        return IN_PROGRESS_STATUSES.stream()
                .map(EprDeclarationOrderStatus::getId)
                .collect(Collectors.toList());
    }

    public static String getName(int value) {

        for (EprDeclarationOrderStatus serveState : EprDeclarationOrderStatus.values()) {
            if (serveState.getId() == value) {
                return serveState.getName();
            }
        }

        throw new RuntimeException("非法的状态枚举值" + value);
    }
}
