package com.guns21.servlet.util;

import com.alibaba.fastjson.JSON;
import com.guns21.result.domain.Result;

import javax.servlet.ServletResponse;
import java.io.*;

/**
 * Created by ljj on 17/6/22.
 */
public class ResponseUtils {
    public static <T> void writeResponse(ServletResponse response, Result<T> result) throws IOException {
        if (response != null) {
            response.setContentType("application/json;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            try (PrintWriter out = response.getWriter()) {
                String message = JSON.toJSONString(result);
                out.print(message);
            }
        }
    }
}
