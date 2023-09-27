package com.guns21.authentication.filter;


import com.guns21.authentication.ext.AuthExtValidator;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.InsufficientAuthenticationException;

import java.io.IOException;

public class ValidatorFilter implements Filter {
    private AuthExtValidator authExtValidator;

    public ValidatorFilter(AuthExtValidator authExtValidator) {
        this.authExtValidator = authExtValidator;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        /**
         * 通用扩展验证，用于其他项目在引用时，自定义validation
         */
        if (!authExtValidator.run((HttpServletRequest) servletRequest)) {
            throw new InsufficientAuthenticationException(authExtValidator.getError());
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
