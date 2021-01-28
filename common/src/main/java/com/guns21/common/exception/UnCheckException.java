package com.guns21.common.exception;

public class UnCheckException extends RuntimeException {
    public UnCheckException() {
        super();
    }

    public UnCheckException(String message) {
        super(message);
    }

    public UnCheckException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnCheckException(Throwable cause) {
        super(cause);
    }

    protected UnCheckException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
