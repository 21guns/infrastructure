package com.guns21.authentication.provider.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.intercept.InterceptorStatusToken;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import java.io.*;

/**
 * Created by ljj on 2017/6/18.
 */
@Service
public class MyFilterSecurityInterceptor extends FilterSecurityInterceptor {
    @Value("${com.ktjr.security.message.access-denied:没有访问权限！}")
    private String accessDeniedMessage;

    @Autowired
    private MyInvocationSecurityMetadataSource myInvocationSecurityMetadataSource;

    @Autowired
    private MyAccessDecisionManager myAccessDecisionManager;


    @Autowired
    private void init() {
        setSecurityMetadataSource(myInvocationSecurityMetadataSource);
        setAccessDecisionManager(myAccessDecisionManager);
    }


    @Override
    public void invoke(FilterInvocation fi) throws IOException, ServletException {
        if (fi.getRequest() != null && fi.getRequest().getAttribute("__spring_security_filterSecurityInterceptor_filterApplied") != null && super.isObserveOncePerRequest()) {
            fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
        } else {
            if (fi.getRequest() != null) {
                fi.getRequest().setAttribute("__spring_security_filterSecurityInterceptor_filterApplied", Boolean.TRUE);
            }

            InterceptorStatusToken token = super.beforeInvocation(fi);

            if (token == null) {
                throw new AuthenticationCredentialsNotFoundException(accessDeniedMessage);
            }

            try {
                fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
            } finally {
                super.finallyInvocation(token);
            }

            super.afterInvocation(token, (Object) null);
        }

    }

    public String getAccessDeniedMessage() {
        return accessDeniedMessage;
    }

    public void setAccessDeniedMessage(String accessDeniedMessage) {
        this.accessDeniedMessage = accessDeniedMessage;
    }

}