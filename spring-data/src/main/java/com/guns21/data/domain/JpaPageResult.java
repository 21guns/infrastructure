package com.guns21.data.domain;


import com.guns21.http.HttpStatus;
import com.guns21.result.domain.PageResult;
import org.springframework.data.domain.Page;

/**
 * @param <T>
 */
public class JpaPageResult<T> extends PageResult<T> {
    private JpaPageResult() {
    }

    public static <T> PageResult<T>  success(Page<T> page) {
        return success(String.valueOf(HttpStatus.OK.value()), HttpStatus.OK.getReasonPhrase());
    }

    /**
     * 通用成功
     *
     * @param code    成功编码
     * @param message 成功信息
     * @param object  对象信息
     * @return 返回成功结果
     */
    public static <T> PageResult<T>  success(String code ,String message, Page<T> page) {
        PageResult<T> instance = getInstance(Boolean.TRUE, message, code, page.getContent());

        //todo set pageSize and current page
        return instance;
    }



}
