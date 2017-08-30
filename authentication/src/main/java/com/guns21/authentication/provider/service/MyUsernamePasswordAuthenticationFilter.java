package com.guns21.authentication.provider.service;

import com.guns21.authentication.provider.ext.AuthExtValidator;
import nl.captcha.Captcha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by ljj on 17/6/28.
 */
@Service
public class MyUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    public boolean isLoginEnabled() {
        return loginEnabled;
    }

    public void setLoginEnabled(boolean loginEnabled) {
        this.loginEnabled = loginEnabled;
    }

    @Value("${com.ktjr.security.login-enabled:false}")
    private boolean loginEnabled;

    @Value("${com.ktjr.security.login:/login}")
    private String login;

    @Value("${com.ktjr.security.captchaEnabled:false}")
    private boolean captchaEnabled;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;

    @Autowired
    private MyAuthenticationFailureHandler myAuthenticationFailureHandler;

    @Autowired
    private AuthExtValidator authExtValidator;


    public MyUsernamePasswordAuthenticationFilter() {
        super();
    }


    @Autowired
    public void init() {
        setAuthenticationManager(authenticationManager);
        setAuthenticationSuccessHandler(myAuthenticationSuccessHandler);
        setAuthenticationFailureHandler(myAuthenticationFailureHandler);
        setFilterProcessesUrl(login);
    }

    @Override
    protected boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response) {
        if (!isLoginEnabled() && request.getRequestURI().equals(login)) {
            return false;
        }

        return super.requiresAuthentication(request, response);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        /**
         * 验证码处理
         * 先判断是否有验证码验证要求，如果有则正常验证，如果没有则跳过。
         */
        if (captchaEnabled) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                Captcha captcha = (Captcha) session.getAttribute(Captcha.NAME);
                String p_captcha = request.getParameter("v_code");
//                if(captcha != null) {
                if (captcha == null || p_captcha == null || !captcha.isCorrect(p_captcha)) {
                    throw new InsufficientAuthenticationException("请传入有效的验证码！");
                }
//                }
            }
        }

        /**
         * 通用扩展验证，用于其他项目在引用时，自定义validation
         */
        if (!authExtValidator.run(request)) {
            throw new InsufficientAuthenticationException(authExtValidator.getError());
        }

        return super.attemptAuthentication(request, response);
    }
}
