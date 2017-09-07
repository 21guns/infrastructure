package com.guns21.authorization.security;

import com.guns21.authentication.provider.util.SecurityAuthUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Iterator;

/**
 * Created by ljj on 2017/6/18.
 */

public class HttpAccessDecisionManager implements AccessDecisionManager {

//    private static final String SUPER_ADMINISTRATOR = "SUPER_ADMIN";

    @Value("${com.ktjr.security.message.access-denied:没有访问权限！}")
    private String accessDeniedMessage;

    /**
     * 判断configAttribute中找角色在authentication中是否存在，如果不存在throws 异常。
     *
     * @paramauthentication
     * @paramobject
     * @paramconfigAttributes
     * @throwsAccessDeniedException
     * @throwsInsufficientAuthenticationException
     */
    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) {

        /**
         * 1.基于authentication中的role信息，与configAttributes中的role信息进行比对，来判断是否有权限。
         */


        if (null == configAttributes || configAttributes.size() == 0) {
            throw new AccessDeniedException(accessDeniedMessage);
        } else {
            //如果权限返回的角色中包括匿名用户角色，则该权限不需验证
            if (configAttributes.stream().map(ca -> ca.getAttribute()).anyMatch(role -> role.equals("ROLE_ANONYMOUS"))) {
                return;
            }
        }

        if (authentication.getAuthorities() == null || authentication.getAuthorities().size() == 0) {
            throw new AccessDeniedException(accessDeniedMessage);
        }

        /** 超级管理员处理 **/
//        if (authentication.getAuthorities().stream()
//                .map(a -> a.getAuthority())
//                .anyMatch(role -> role.equals(SecurityAuthUtil.SUPER_ADMINISTRATOR))) {
//            return;
//        }

        /** 用户权限检查 **/


        ConfigAttribute c;
        String needRole;
        for (Iterator<ConfigAttribute> iter = configAttributes.iterator(); iter.hasNext(); ) {
            c = iter.next();
            needRole = c.getAttribute();
            for (GrantedAuthority ga : authentication.getAuthorities()) {
                if (needRole.trim().equals(ga.getAuthority())) {
                    return;
                }
            }
        }

        throw new AccessDeniedException(accessDeniedMessage);
    }

    /**
     * @return
     * @paramattribute
     */
    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    /**
     * @return
     * @paramclazz
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }

}
