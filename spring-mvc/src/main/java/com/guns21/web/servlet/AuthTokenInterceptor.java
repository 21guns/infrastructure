package com.guns21.web.servlet;

import com.alibaba.fastjson.JSON;
import com.guns21.domain.result.light.Result;
import com.guns21.web.annotation.AuthAnnotation;
import com.guns21.web.constant.SpringConstant;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.io.*;

/**
 *
 */
public class AuthTokenInterceptor implements HandlerInterceptor {

    private static Logger logger = LoggerFactory.getLogger(AuthTokenInterceptor.class);

    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response, Object arg2, Exception arg3)
            throws Exception {
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object arg2, ModelAndView arg3) throws Exception {
    }

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        if (handler.getClass().isAssignableFrom(HandlerMethod.class)) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            AuthAnnotation authAnnotation = handlerMethod.getMethodAnnotation(AuthAnnotation.class);
            if (authAnnotation == null) {
                return true;
            } else {
                return validateToken(request, response);
            }
        }

        return true;
    }

    protected boolean validateToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
    /*
     * 1.当客户端连接服务器是都会生成session,都有session id
     * 2.只有登陆成功才会返回token,并且设置session中的LOGIN_USER属性为当前登录用户
     * 3.当有人拿到session id时需要判断LOGIN_USER属性是否为空,来判断是否登录过
     * */
        String tokenId = request.getHeader(SpringConstant.TOKEN_KEY);
        HttpSession currentSession = request.getSession(false);
        if (null == tokenId && null == currentSession) {
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(JSON.toJSONString(Result.fail("未登陆", "0010003")));
            return false;
        } else if (null != tokenId && null == currentSession) {
            //token过期
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(JSON.toJSONString(Result.fail("token过期", "0010004")));
            return false;
        } else if (null != tokenId && null != currentSession) {
            //授权通过
            return true;
        } else {
            logger.error("授权验证未知情况出现,需要验证该情况");
            return false;
        }
    }
}
