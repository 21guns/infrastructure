package com.guns21.authorization.security;

/**
 * Created by ljj on 17/6/19.
 */

import com.guns21.authorization.ResourceRoleMapping;
import com.guns21.common.util.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RedisInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {
    private static final Logger LOGGER = LoggerFactory.getLogger(RedisInvocationSecurityMetadataSource.class);
    private String permissionRedisKey = "permission_redis_key";

    @Resource(name = "sessionRedisTemplate")
    private RedisTemplate<String, Map<String, List<String>>> template;

    @Autowired
    private ResourceRoleMapping resourceRoleMapping;
    //    private HashMap<String, Collection<ConfigAttribute>> map =null;


    /**
     * 获取访问权限的角色集合
     * 返回空集合是表示白名单，任何人都有权限
     *
     * @return
     * @paramobject
     * @throwsIllegalArgumentException
     */
    @Override
    public Collection<ConfigAttribute> getAttributes(Object object)
            throws IllegalArgumentException {
        Collection<ConfigAttribute> configAttributes = new ArrayList<>();
        HttpServletRequest request = ((FilterInvocation) object).getHttpRequest();
        //取redis中的数据
        HashOperations<String, String, List<String>> ops = template.opsForHash();
        String requestURI = request.getRequestURI();
        List<String> roles = ops.get(permissionRedisKey, requestURI); //TODO 添加过期时间
//        List<String> roles = Collections.EMPTY_LIST;
        if (ObjectUtils.isEmpty(roles)) {
            //提供数据来源
            roles = resourceRoleMapping.listRole(requestURI);
            ops.put(permissionRedisKey, requestURI, roles);
        }

        if (null == roles || roles.size() == 0) {
            LOGGER.warn("url {} hasn't roles", requestURI);
            return Collections.EMPTY_LIST;
        }

        return roles.stream().map(r -> new SecurityConfig(r))
                .collect(Collectors.toList());

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
        return FilterInvocation.class.isAssignableFrom(clazz);
    }
}
