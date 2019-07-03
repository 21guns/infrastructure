package com.guns21.common.exception;

/**
 * Created by jliu on 2016/12/7.
 */
public class IllegalInputArgumentException extends CommonException {

    private String code;

    public IllegalInputArgumentException(String code, String message) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
