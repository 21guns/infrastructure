package com.guns21.data.domain.result;


import com.guns21.domain.result.light.PageResult;
import com.guns21.http.HttpStatus;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @param <T>
 */
public class JpaPageResult<T> extends PageResult<T> {
    private JpaPageResult() {
    }

    public static <T> PageResult<T> success(Page<T> page) {
        return success(String.valueOf(HttpStatus.OK.value()), HttpStatus.OK.getReasonPhrase(), page);
    }

    /**
     * form T to R type
     * @param page
     * @param mapper
     * @param <T>
     * @param <R>
     * @return
     */
    public static <T, R> PageResult<R> success(Page<T> page, Function<? super T, ? extends R> mapper) {
        //set page property
        PageResult success = success(String.valueOf(HttpStatus.OK.value()), HttpStatus.OK.getReasonPhrase(), page);
        List<R> collect = page.getContent().stream().map(mapper).collect(Collectors.toList());
        success.setData(collect);
        return success;
    }

    /**
     * 通用成功
     *
     * @param code    成功编码
     * @param message 成功信息
     * @param page  对象信息
     * @return 返回成功结果
     */
    public static <T> PageResult<T> success(String code, String message, Page<T> page) {
        PageResult<T> instance = getInstance(Boolean.TRUE, message, code, page.getContent());

        instance.setTotals(page.getTotalElements());
        instance.setPage(page.getNumber());
        instance.setPageSize(page.getSize());
        return instance;
    }


}
