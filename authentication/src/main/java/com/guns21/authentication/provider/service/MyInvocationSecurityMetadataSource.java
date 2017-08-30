package com.guns21.authentication.provider.service;

/**
 * Created by ljj on 17/6/19.
 */

import com.guns21.authentication.provider.repository.SecurityRepository;
import com.guns21.authentication.provider.util.SecurityAuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
//@Log4j
public class MyInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {
    @Value("${com.ktjr.security.permit-pages:null}")
    private String[] permitPages;

    private String anonymousRole = "ROLE_ANONYMOUS";

    @Autowired
    private SecurityRepository securityRepository;

//    private HashMap<String, Collection<ConfigAttribute>> map =null;


    /**
     * 获取访问权限的角色集合
     *
     * @return
     * @paramobject
     * @throwsIllegalArgumentException
     */
    @Override
    public Collection<ConfigAttribute> getAttributes(Object object)
            throws IllegalArgumentException {
        Collection<ConfigAttribute> configAttributes = new ArrayList<>();

        //获取用户权限信息
        HashMap<String, Collection<ConfigAttribute>> map = securityRepository.listGetPermissions();

        if (map == null) {
            map = new HashMap<>();
        }

        //把超级管理员权限写入
        Collection<ConfigAttribute> superRoleList = new ArrayList<>();
        superRoleList.add(new SecurityConfig(SecurityAuthUtil.SUPER_ADMINISTRATOR));
        map.put("/**", superRoleList);

        //添加匿名访问
        if (permitPages != null) {
            for (String p : permitPages) {
                Collection<ConfigAttribute> temp = map.get(p);
                if (temp != null) {
                    temp.add(new SecurityConfig(anonymousRole));
                } else {
                    temp = new ArrayList<>();
                    temp.add(new SecurityConfig(anonymousRole));
                }
                map.put(p, temp);
            }
        }


        //获取当前权限的角色信息
        HttpServletRequest request = ((FilterInvocation) object).getHttpRequest();
        AntPathRequestMatcher matcher;
        String resUrl;
//        for(Iterator<String> iter = map.keySet().iterator(); iter.hasNext(); ) {
//            resUrl = iter.next();
//            matcher = new AntPathRequestMatcher(resUrl);
//            if(matcher.matches(request)) {
//                configAttributes.addAll(map.get(resUrl));
//                //return map.get(resUrl);
//            }
//        }

        for (Map.Entry<String, Collection<ConfigAttribute>> entry : map.entrySet()) {
            resUrl = entry.getKey();
            matcher = new AntPathRequestMatcher(resUrl);
            if (matcher.matches(request)) {
                configAttributes.addAll(entry.getValue());
            }
        }


        return configAttributes;
    }

    /**
     * @return
     */
    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}
