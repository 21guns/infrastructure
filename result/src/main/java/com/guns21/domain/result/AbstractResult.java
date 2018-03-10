package com.guns21.domain.result;

import lombok.Data;

/**
 * Created by jliu on 16/7/19.
 */
@Data
public class AbstractResult<T> {

    protected String code;
    protected Boolean success;
    protected String message;
    //entity,list,page,message
    protected String type;
    protected T data;

    public enum Code {

        TYPE_VIOLATION("1020", "数据类型不合法"),
        MISS_PARAMETER("1021", "丢失参数");

        protected String code;
        protected String text;

        Code(String code, String text)
        {
            this.code = code;
            this.text = text;
        }

        public String getCode() {
            return code;
        }

        public String getText() {
            return text;
        }
    }
}
