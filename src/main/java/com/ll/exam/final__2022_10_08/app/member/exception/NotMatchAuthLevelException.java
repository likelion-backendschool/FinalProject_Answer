package com.ll.exam.final__2022_10_08.app.member.exception;

public class NotMatchAuthLevelException extends RuntimeException {
    public NotMatchAuthLevelException() {
    }

    public NotMatchAuthLevelException(String message) {
        super(message);
    }

    public NotMatchAuthLevelException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotMatchAuthLevelException(Throwable cause) {
        super(cause);
    }

    public NotMatchAuthLevelException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
