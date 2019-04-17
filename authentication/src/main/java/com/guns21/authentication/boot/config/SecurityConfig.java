package com.guns21.authentication.boot.config;

import com.guns21.common.util.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Objects;

@Configuration
public class SecurityConfig {

    /**
     * false:标识禁用匿名访问
     */
    @Value("${com.guns21.security.anonymous:false}")
    private boolean anonymous;

    @Value("${com.guns21.session.maximum:1}")
    private int maximumSessions;

    public boolean isAnonymous() {
        return !anonymous;
    }

    public int getMaximumSessions() {
        return maximumSessions;
    }

    @Configuration
    @ConfigurationProperties(prefix = "com.guns21.security.permit")
    public static class SecurityPermitConfig {

        private String[] permitPages;

        public void setPages(List<String> permitPages) {
            if (ObjectUtils.nonEmpty(permitPages)) {
                this.permitPages = permitPages.toArray(new String[0]);
            } else {
                this.permitPages = null;
            }
        }

        public String[] getPermitPages() {
            return permitPages;
        }
    }
}
