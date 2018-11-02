package com.guns21.web.advice;


import com.google.common.base.Throwables;
import com.guns21.domain.result.light.Result;
import com.guns21.support.exception.IllegalInputArgumentException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * 异常处理
 * 即把@ControllerAdvice注解内部使用@ExceptionHandler、@InitBinder、@ModelAttribute注解的方法应用到所有的
 *
 * @RequestMapping注解的方法。非常简单，不过只有当使用@ExceptionHandler最有用，另外两个用处不大。
 */
@ControllerAdvice(annotations = RestController.class)
class ApiExceptionHandlerAdvice {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiExceptionHandlerAdvice.class);

    /**
     * @param exception
     * @return
     */
    @ExceptionHandler(value = {NoSuchElementException.class})
    @ResponseBody
    public Result dataNotFound(Exception exception) {
        LOGGER.error("dataNotFound", exception);
        return Result.fail("0000002", "输入数据无效");
    }


    /**
     * @param exception
     * @return
     */
    @ExceptionHandler(value = {NullPointerException.class})
    @ResponseBody
    public Result nullPointerException(Exception exception) {
        LOGGER.error("nullPointerException", exception);
        return Result.fail(firstThrowableAsString(exception));
    }

    /**
     * 处理validation 注解校验信息
     */
    @ExceptionHandler(value = {BindException.class })
    @ResponseBody
    public Result exceptionBind(BindException exception, BindingResult bindingResult) {
        return validation(bindingResult.getFieldErrors());
    }
    /**
     * 处理validation 注解校验信息
     */
    @ExceptionHandler(value = {MethodArgumentNotValidException.class })
    @ResponseBody
    public Result exceptionValid(MethodArgumentNotValidException valiException) {
        return validation(valiException.getBindingResult().getFieldErrors());
    }

    /**
     * 错误信息描述：错误码|错误描述
     *
     * @param fieldErrors
     * @return
     */
    protected Result validation(List<FieldError> fieldErrors) {
        for (FieldError fieldError : fieldErrors) {
            String message = fieldError.getDefaultMessage();
            LOGGER.error("字段[{}] [{}]", fieldError.getField(), message);
            if (!message.contains(":")) {
                return Result.fail(message);
            }
            String[] split = StringUtils.split(message, ":");
            if (split.length == 2 && NumberUtils.isNumber(split[0])) {
                return Result.fail(split[1], split[0]);
            } else {
                return Result.fail(message);
            }
        }
        return Result.fail("解析message系统异常。");
    }

    /**
     * 类型转换异常
     *
     * @param exception
     * @param request
     * @return
     */
    @ExceptionHandler(value = TypeMismatchException.class)
    @ResponseBody
    public Result typeMismatchException(TypeMismatchException exception, WebRequest request) {
        Result resultData = Result.fail();
        resultData.setCode(Result.Code.TYPE_VIOLATION.getCode());
        resultData.setMessage(Result.Code.TYPE_VIOLATION.getText() + ":参数需要[" + exception.getRequiredType().getSimpleName() + "]类型，但传入值为{" + exception.getValue() + "}");

        LOGGER.error("type mismatch : {} ", resultData.getMessage());
        return resultData;
    }

    /**
     * 丢失必填参数异常，参数注解使用@RequestParam、@PathVariable，@Valid验证不会在本函数处理
     *
     * @param exception
     * @param request
     * @return
     */
    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    @ResponseBody
    public Result missingServletRequestParameterException(MissingServletRequestParameterException exception, WebRequest request) {
        Result resultData = Result.fail();
        resultData.setCode(Result.Code.MISS_PARAMETER.getCode());
        resultData.setMessage(exception.getMessage());
        LOGGER.error("missing servlet request parameter : {} ",resultData.getMessage());
        return resultData;
    }

    /**
     * Handle exceptions thrown by handlers.
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result exception(Exception exception, WebRequest request) {
        LOGGER.error("exception", exception);
        if (exception instanceof IllegalInputArgumentException) {
            return Result.fail(exception.getMessage(), ((IllegalInputArgumentException) exception).getCode());
        } else {
            return Result.fail(Throwables.getRootCause(exception).getLocalizedMessage());
        }
    }


    private String firstThrowableAsString(Throwable throwable) {
        List<Throwable> causalChain = Throwables.getCausalChain(throwable);
        String causal = causalChain.size() >= 1 ? Throwables.getStackTraceAsString(causalChain.get(0)) : "空异常信息";
        return StringUtils.left(causal, 200);
    }
}