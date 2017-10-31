package com.guns21.result.domain;


import com.guns21.http.HttpStatus;

import java.util.List;
import java.util.Objects;

/**
 * @param <T>
 */
public class PageResult<T> extends AbstractResult<List<T>> {
    protected int page; //当前页数
    protected int pageSize; //每页条数
    protected long totals;

    protected static PageResult getInstance() {
        return new PageResult();
    }

    protected static <T> PageResult<T> getInstance(Boolean aTrue, String message, String code, List<T> object) {
        PageResult result = getInstance();
        result.setSuccess(aTrue);
        result.setMessage(message);
        result.setCode(code);
        result.setData(object);
        return result;
    }

    public static <T> PageResult<T> success() {
        return success(String.valueOf(HttpStatus.OK.value()), HttpStatus.OK.getReasonPhrase());
    }

    public static <T> PageResult<T> success(List<T> object) {
        return success(String.valueOf(HttpStatus.OK.value()), HttpStatus.OK.getReasonPhrase(), object);
    }

    /**
     * 通用成功
     *
     * @param message 成功信息
     * @return 返回成功结果
     */
    public static <T> PageResult<T> success(String message) {
        return success(String.valueOf(HttpStatus.OK.value()), message);
    }

    /**
     * 通用成功
     *
     * @param code    成功编码
     * @param message 成功信息
     * @return 返回成功结果
     */
    public static <T> PageResult<T> success(String code, String message) {
        return success(code, message, null);
    }

    /**
     * 通用成功
     *
     * @param code    成功编码
     * @param message 成功信息
     * @param object  对象信息
     * @return 返回成功结果
     */
    public static <T> PageResult<T> success(String code, String message, List<T> object) {
        return getInstance(Boolean.TRUE, message, code, object);
    }


    /**
     * 通用异常
     *
     * @param message 失败信息
     * @return 返回失败信息和500
     */
    public static <T> PageResult<T> fail(String message) {
        return fail(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), message);
    }

    public static <T> PageResult<T> fail() {
        return fail(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
    }

    /**
     * 通用异常
     *
     * @param message 失败信息
     * @return 返回失败结果
     */
    public static <T> PageResult<T> fail(String code, String message) {
        return fail(code, message, null);
    }

    /**
     * 通用异常
     *
     * @param code    失败编码
     * @param message 失败信息
     * @param object  对象信息
     * @return 返回失败结果
     */
    public static <T> PageResult<T> fail(String code, String message, List object) {
        return getInstance(Boolean.FALSE, message, code, object);
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public long getTotals() {
        return totals;
    }

    public void setTotals(long totals) {
        this.totals = totals;
    }
}
