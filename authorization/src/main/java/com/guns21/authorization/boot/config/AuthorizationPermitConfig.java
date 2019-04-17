package com.guns21.authorization.boot.config;

import com.guns21.common.util.ObjectUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import java.util.List;
import java.util.Objects;

/**
 * com.guns21.security.auth.permit 和 com.guns21.security.permit的区别是前一个配置有security的各种filter
 * WebSecurity.IgnoredRequestConfigurer 直接就忽略，不对url追加spring security各种filter,不进行鉴权
 * http.authorizeRequests().antMatchers("/api/**").permitAll(); 会追加spring security各种filter，进行鉴权
 */
@Configuration
@EnableWebSecurity
@Order(101)
@ConfigurationProperties(prefix = "com.guns21.security.auth.permit")
public class AuthorizationPermitConfig extends WebSecurityConfigurerAdapter {
    private String[] permitPages;

    @Override
    public void init(WebSecurity web) throws Exception {
        if (Objects.nonNull(permitPages)) {
            super.init(web);
        }
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests().antMatchers(permitPages).permitAll()
                .and().csrf().disable();
    }
    public void setPages(List<String> permitPages) {
        if (ObjectUtils.nonEmpty(permitPages)) {
            this.permitPages = permitPages.toArray(new String[0]);
        } else {
            this.permitPages = null;
        }
    }

}
