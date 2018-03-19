package com.guns21.web.method.resolver;

import com.guns21.web.bind.annotation.CurrentUser;
import com.guns21.web.constant.SpringConstant;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

public class CurrentUserMethodArgumentResolver implements HandlerMethodArgumentResolver {

    public CurrentUserMethodArgumentResolver() {
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        if (parameter.hasParameterAnnotation(CurrentUser.class)) {
            return true;
        }
        return false;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return request.getSession(false) == null? null:request.getSession(false).getAttribute(SpringConstant.LOGIN_USER);
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        return authentication.getPrincipal();
    }
}
