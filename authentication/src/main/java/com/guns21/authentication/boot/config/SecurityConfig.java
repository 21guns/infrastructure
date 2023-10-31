package com.guns21.authentication.boot.config;

import com.guns21.common.util.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.List;

@Configuration
public class SecurityConfig {
    @Value("${com.guns21.security.login:/login}")
    private String login;
    @Value("${com.guns21.security.logout:/logout}")
    private String logout;
    @Value("${com.guns21.security.username-parameter:username}")
    private String usernameParameter;
    @Value("${com.guns21.security.password-parameter:password}")
    private String passwordParameter;
    /**
     * false:标识禁用匿名访问
     */
    @Value("${com.guns21.security.anonymous:false}")
    private boolean anonymous;

    @Value("${com.guns21.session.maximum:1}")
    private int maximumSessions;

    public String getLogin() {
        return login;
    }

    public String getLogout() {
        return logout;
    }

    public String getUsernameParameter() {
        return usernameParameter;
    }

    public String getPasswordParameter() {
        return passwordParameter;
    }

    public boolean isAnonymous() {
        return anonymous;
    }

    public int getMaximumSessions() {
        return maximumSessions;
    }

    @Configuration
    @ConfigurationProperties(prefix = "com.guns21.security.permit")
    public static class SecurityPermitConfig {

        private List<String> permitPages;

        public void setPages(List<String> permitPages) {
            if (ObjectUtils.nonEmpty(permitPages)) {
                this.permitPages = permitPages;
            } else {
                this.permitPages = Collections.emptyList();
            }
        }

        public List<String> getPermitPages() {
            return permitPages;
        }
    }
}
