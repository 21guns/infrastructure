package com.ktjr.security.provider.util;

import com.alibaba.fastjson.JSON;
import com.guns21.result.domain.Result;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * Created by ljj on 17/6/22.
 */
public class ResponseUtil {
    public static <T> void writeResponse(HttpServletResponse response, Result<T> result) throws IOException {
        if (response != null) {
            response.setContentType("text/html;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            try (PrintWriter out = response.getWriter()) {
                String message = JSON.toJSONString(result);
                out.print(message.getBytes("UTF-8"));
            }
        }
    }
}
