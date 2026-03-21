package com.genetics.common;

import lombok.Getter;

/**
 * 业务异常
 */
@Getter
public class BusinessException extends RuntimeException {

    private final int code;

    public BusinessException(String message) {
        super(message);
        this.code = -1;
    }

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    /**
     * 抛出业务异常
     */
    public static void throwException(String message) {
        throw new BusinessException(message);
    }

    /**
     * 抛出业务异常
     */
    public static void throwException(int code, String message) {
        throw new BusinessException(code, message);
    }
}
