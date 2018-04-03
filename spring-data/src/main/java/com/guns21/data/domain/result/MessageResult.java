package com.guns21.data.domain.result;


import com.github.pagehelper.PageInfo;
import com.guns21.domain.result.AbstractResult;
import com.guns21.domain.result.ResultType;
import com.guns21.http.HttpStatus;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ljj on 17/6/7.
 */
public class MessageResult<T> extends AbstractResult<T> {

    private MessageResult() {
        super();
    }

    public static <T> MessageResult<T> successPage(List<T> list) {
        PageInfo page = new PageInfo(list);
        PageData pageData = PageData.builder()
                .totalPages(page.getPages())
                .totalElements(page.getTotal())
                .pageData(page.getList())
                .page(page.getPageNum())
                .size(page.getSize())
                .build();

        return success(String.valueOf(HttpStatus.OK.value()), HttpStatus.OK.getReasonPhrase(), ResultType.PAGE, (T) pageData);
    }

    public static <T> MessageResult<T> success() {
        return success(String.valueOf(HttpStatus.OK.value()), HttpStatus.OK.getReasonPhrase());
    }

    public static <T,R> MessageResult<R> success(T object) {

        return success(String.valueOf(HttpStatus.OK.value()), HttpStatus.OK.getReasonPhrase(), object);
    }

    /**
     * 输入参数是以key:value为键值对的数据.
     *
     * @param objects 输入参数是以key:value为键值对的数据
     * @return Result
     */
    public static MessageResult<Map> successMap(Object... objects) {
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
    public static <T,R> MessageResult<R> success(String message) {
        return success(String.valueOf(HttpStatus.OK.value()), message);
    }

    /**
     * 通用成功.
     *
     * @param code    成功编码
     * @param message 成功信息
     * @return 返回成功结果
     */
    public static <T,R> MessageResult<R> success(String code, String message) {
        return success(code, message, ResultType.MESSAGE, null);
    }

    public static <T,R> MessageResult<R> success(String code, T object) {
        return success(code, HttpStatus.OK.getReasonPhrase(), object);
    }
    public static <T,R> MessageResult<R> success(String code, String message, T object) {
        if (object instanceof Collection) {
            return success(code, message,ResultType.LIST, object);
        }
        return success(code, message,ResultType.ENTITY, object);

    }
    private static <T,R> MessageResult<R> success(String message, ResultType resultType, T object) {
        return success(String.valueOf(HttpStatus.OK.value()), message,resultType,  object);
    }

    /**
     * 通用成功.
     *
     * @param code    成功编码
     * @param message 成功信息
     * @param object  对象信息
     * @return 返回成功结果
     */
    private static <T,R> MessageResult<R> success(String code, String message, ResultType resultType, T object) {
        return getInstance(Boolean.TRUE, message, code, resultType, object);
    }

    /**
     * 通用异常.
     *
     * @param message 失败信息
     * @return 返回失败信息和４０３
     */
    public static <T> MessageResult<T> fail403(String message) {
        return fail(String.valueOf(HttpStatus.FORBIDDEN.value()), message);
    }

    /**
     * 通用异常.
     *
     * @param message 失败信息
     * @return 返回401失败
     */
    public static <T> MessageResult<T> fail401(String message) {
        return fail(String.valueOf(HttpStatus.UNAUTHORIZED.value()), message);
    }


    /**
     * 通用异常.
     *
     * @param message 失败信息
     * @return 返回失败信息和500
     */
    public static <T> MessageResult<T> fail(String message) {
        return fail(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), message);
    }

    public static <T> MessageResult<T> fail() {
        return fail(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
    }

    /**
     * 通用异常.
     *
     * @param message 失败信息
     * @return 返回失败结果
     */
    public static <T> MessageResult<T> fail(String code, String message) {
        return fail(code, message, null);
    }

    public static <T> MessageResult<T> fail(String message, T object) {
        return fail(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), message, object);
    }

    /**
     * 通用异常.
     *
     * @param code    失败编码
     * @param message 失败信息
     * @param object  对象信息
     * @return 返回失败结果
     */
    public static <T> MessageResult<T> fail(String code, String message, T object) {
        return getInstance(Boolean.FALSE, message, code, ResultType.MESSAGE, object);
    }

    private static <T,R> MessageResult<R> getInstance(Boolean aTrue, String message, String code, ResultType resultType, T object) {

        MessageResult result = new MessageResult();
        result.setSuccess(aTrue);
        result.setMessage(message);
        result.setCode(code);
        result.setType(resultType.name().toLowerCase());
        result.setData(object);

        return result;
    }
}
