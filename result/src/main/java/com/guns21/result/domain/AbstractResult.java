package com.guns21.result.domain;

/**
 * Created by jliu on 16/7/19.
 */
public class AbstractResult<T> {

    protected String code;
    protected Boolean success;
    protected String message;
    protected T data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public AbstractResult setData(T data) {
        this.data = data;
        return this;
    }
}
