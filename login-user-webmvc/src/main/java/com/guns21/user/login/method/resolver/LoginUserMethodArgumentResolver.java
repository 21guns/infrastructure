package com.guns21.user.login.method.resolver;

import com.guns21.common.exception.CurrentUserIsNullException;
import com.guns21.user.login.annotation.CurrentUser;
import com.guns21.user.login.constant.LoginConstant;
import com.guns21.user.login.domain.UserInfo;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Objects;

public class LoginUserMethodArgumentResolver implements HandlerMethodArgumentResolver {

    public LoginUserMethodArgumentResolver() {
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(CurrentUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        CurrentUser methodAnnotation = parameter.getParameterAnnotation(CurrentUser.class);
        Object currentUser = request.getSession(false) == null ? null : request.getSession(false).getAttribute(LoginConstant.LOGIN_USER);
        if (methodAnnotation.required() && Objects.isNull(currentUser)) {
//            request.getSession(false).invalidate();
          throw new CurrentUserIsNullException();
        }
        if (currentUser != null && currentUser instanceof UserInfo) {
            UserInfo currentUserInfo = (UserInfo) currentUser;
            if (currentUserInfo.getManagedUserIds() == null || currentUserInfo.getManagedUserIds().isEmpty()) {
                currentUserInfo.setManagedUserIds(Arrays.asList(currentUserInfo.getId()));
            }
        }
        return currentUser;
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        return authentication.getPrincipal();
    }
}
