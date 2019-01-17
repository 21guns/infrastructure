package com.guns21.web.controller;

import com.guns21.data.domain.result.MessageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;
import java.util.Map;

/**
 * controller通用错误处理
 * @see org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController
 */
@RestController
@Slf4j
public class ErrorController  {

    @Autowired
    private ErrorAttributes errorAttributes;

    @RequestMapping(value = "/error", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
    public MessageResult error(HttpServletRequest request) {
        WebRequest webRequest = new ServletWebRequest(request);
        Map<String, Object> errorAttributes = this.errorAttributes.getErrorAttributes(webRequest, false);
        Integer status=(Integer)errorAttributes.get("status");
        String path = (String)errorAttributes.get("path");
        String messageFound = (String)errorAttributes.get("message");
        String message = MessageFormat.format("url {0} error messages {1}", path, messageFound);
        log.error(message);
        return MessageResult.fail(String.valueOf(status), messageFound);
    }
}
