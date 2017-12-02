package com.guns21.result.domain;

import com.guns21.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * 接口统一返回结果
 */
public class Result<T> extends AbstractResult<T> {

    private Result() {
    }

    public static <T> Result<T> getInstance() {
        return new Result<T>();
    }

    public static <T> Result<T> success() {
        return success(String.valueOf(HttpStatus.OK.value()), HttpStatus.OK.getReasonPhrase());
    }

    public static <T> Result<T> success(T object) {
        return success(String.valueOf(HttpStatus.OK.value()), HttpStatus.OK.getReasonPhrase(), object);
    }

    /**
     * 输入参数是以key:value为键值对的数据.
     *
     * @param objects 输入参数是以key:value为键值对的数据
     * @return Result
     */
    public static Result<Map> success(Object... objects) {
        if (null != objects && objects.length > 0 && objects.length % 2 == 0) {
            Map<Object, Object> map = new HashMap<Object, Object>();
            for (int i = 0; i < objects.length; i += 2) {
                map.put(objects[i], objects[i + 1]);
            }
            return success(map);
        }
        throw new IllegalArgumentException("objects is illegal");
    }

    /**
     * 通用成功.
     *
     * @param message 成功信息
     * @return 返回成功结果
     */
    public static <T> Result<T> success(String message) {
        return success(String.valueOf(HttpStatus.OK.value()), message);
    }

    /**
     * 通用成功.
     *
     * @param code    成功编码
     * @param message 成功信息
     * @return 返回成功结果
     */
    public static <T> Result<T> success(String code, String message) {
        return success(code, message, null);
    }

    public static <T> Result<T> success(String message, T object) {
        return success(String.valueOf(HttpStatus.OK.value()), message, object);
    }

    /**
     * 通用成功.
     *
     * @param code    成功编码
     * @param message 成功信息
     * @param object  对象信息
     * @return 返回成功结果
     */
    public static <T> Result<T> success(String code, String message, T object) {
        return getInstance(Boolean.TRUE, message, code, object);
    }

    private static <T> Result<T> getInstance(Boolean aTrue, String message, String code, T object) {
        Result<T> result = getInstance();
        result.setSuccess(aTrue);
        result.setMessage(message);
        result.setCode(code);
        result.setData(object);
        return result;
    }

    /**
     * 通用异常.
     *
     * @param message 失败信息
     * @return 返回失败信息和４０３
     */
    public static <T> Result<T> fail403(String message) {
        return fail(String.valueOf(HttpStatus.FORBIDDEN.value()), message);
    }

    /**
     * 通用异常.
     *
     * @param message 失败信息
     * @return 返回401失败
     */
    public static <T> Result<T> fail401(String message) {
        return fail(String.valueOf(HttpStatus.UNAUTHORIZED.value()), message);
    }


    /**
     * 通用异常.
     *
     * @param message 失败信息
     * @return 返回失败信息和500
     */
    public static <T> Result<T> fail(String message) {
        return fail(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), message);
    }

    public static <T> Result<T> fail() {
        return fail(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
    }

    /**
     * 通用异常.
     *
     * @param message 失败信息
     * @return 返回失败结果
     */
    public static <T> Result<T> fail(String code, String message) {
        return fail(code, message, null);
    }

    /**
     * 通用异常.
     *
     * @param code    失败编码
     * @param message 失败信息
     * @param object  对象信息
     * @return 返回失败结果
     */
    public static <T> Result<T> fail(String code, String message, T object) {
        return getInstance(Boolean.FALSE, message, code, object);
    }


}
