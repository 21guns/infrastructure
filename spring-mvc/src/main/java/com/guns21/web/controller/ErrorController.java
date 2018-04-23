package com.guns21.web.controller;

import com.guns21.data.domain.result.MessageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * controller通用错误处理
 * @see org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController
 */
@RestController
@Slf4j
public class ErrorController {

    @RequestMapping(value = "/error", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
    public MessageResult error(HttpServletRequest request) {
        HttpStatus status = getStatus(request);
        log.error("error {} ", status);
        return MessageResult.fail(String.valueOf(status.value()), status.getReasonPhrase());
    }

    protected HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request
                .getAttribute("javax.servlet.error.status_code");
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        try {
            return HttpStatus.valueOf(statusCode);
        }
        catch (Exception ex) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }
}
