package com.guns21.authorization.security;

/**
 * Created by ljj on 17/6/19.
 */

import com.guns21.authorization.ResourceRoleMapping;
import com.guns21.authorization.domain.AccessResource;
import com.guns21.common.util.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class RedisInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {
    private static final Logger LOGGER = LoggerFactory.getLogger(RedisInvocationSecurityMetadataSource.class);
    private String permissionRedisKey = "permission_redis_key";

    @Resource(name = "redisTemplate")
    private RedisTemplate<String, Map<String, List<String>>> template;
    @Autowired
    private ResourceRoleMapping resourceRoleMapping;

    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    @Autowired
    private com.guns21.authentication.boot.config.SecurityConfig securityConfig;

    private static final String ROLE_ANONYMOUS = "ROLE_ANONYMOUS";

    private static final String SUPER_ADMIN = "SUPER_ADMIN";
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
        HttpServletRequest request = ((FilterInvocation) object).getHttpRequest();
        List<String> roles =  Collections.EMPTY_LIST;
        String requestURI = request.getRequestURI();
        String method = request.getMethod();

        Map<RequestMappingInfo, HandlerMethod> pathMatcher = requestMappingHandlerMapping.getHandlerMethods();
        RequestMappingInfo matchingCondition = null;

        for(Iterator it = pathMatcher.keySet().iterator(); it.hasNext(); ) {
            RequestMappingInfo requestMappingInfo = (RequestMappingInfo) it.next();
            RequestMappingInfo condition = requestMappingInfo.getMatchingCondition(request);
            if (Objects.nonNull(condition) && Objects.equals(requestURI, condition.toString())) {
                break;
            }else if(Objects.nonNull(condition)){
                matchingCondition = condition;
            }
        }
        if (Objects.nonNull(matchingCondition)) {
            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                throw new AuthenticationCredentialsNotFoundException(
                        "An Authentication object was not found in the SecurityContext");
            }

            String key = matchingCondition.toString();
            //取redis中的数据
            BoundHashOperations<String, String,  List<String>> ops = template.boundHashOps(permissionRedisKey);

            //TODO 添加过期时间
            roles = ops.get(key);
            if (ObjectUtils.isEmpty(roles)) {
                //  使用ant表达式 /api/usermanage/v1/org/{no:MD[0-9]{19}}/salesman --> /api/usermanage/v1/org/*/salesman
                List<String> resourceList = matchingCondition.getPatternsCondition().getPatterns().stream()
                        .map(s -> replaceBrace(s, "*", Arrays.asList("{", "}")))
                        .collect(Collectors.toList());
                //提供数据来源
                List<AccessResource> accessResources = resourceRoleMapping.listRole(requestURI, method);
                if (Objects.nonNull(accessResources)) {
                    for (int i = 0; i < accessResources.size(); i++) {
                        AccessResource accessResource = accessResources.get(i);
                        //访问资源
                        if (AccessResource.FULL_RESOURCE.equals(accessResource.getPermissionUrl())
                                || resourceList.contains(accessResource.getPermissionUrl()) ) {
                            //比较访问方式
                            if (AccessResource.FULL_ACCESS.equals(accessResource.getRequestMethod())
                                    || matchingCondition.getMethodsCondition().getMethods().contains(RequestMethod.valueOf(accessResource.getRequestMethod()))) {
                                roles = accessResource.getRole();
                                ops.put(key, roles);
                                break;
                            }
                        }
                    }
                }
            }
        } else {
            LOGGER.error("Not find url [{}]:[{}]", method, requestURI);
        }
//        String path = request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE).toString();
//        String path1 = request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE).toString();
//        String path2 = request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE).toString();
//        String path3 = request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE).toString();
//        String path4 = request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE).toString();

        /**
         * 如果对应的资源没有配置角色：
         * 1.返回空集合是白名单
         * 2.返回非空集合是黑名单,匿名访问时是黑名单
         */
        if (null == roles || roles.size() == 0) {
            LOGGER.warn("url {} hasn't roles and return SUPER_ADMIN's role", requestURI);
            if (securityConfig.isAnonymous()) {
                return Collections.singleton(new SecurityConfig(ROLE_ANONYMOUS));
            } else {
                return Collections.singleton(new SecurityConfig(SUPER_ADMIN));
            }
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

    public static String replaceBrace(String str, String replace, final List<String> contains) {
        return "/" + Arrays.asList(StringUtils.split(str, "/")).stream().reduce((s, s2) -> {
            String finalS1 = s;
            //当完全匹配时替换字符
            if (contains.stream().allMatch(s1 -> finalS1.contains(s1))) {
                s = replace;
            }
            String finalS = s2;
            if (contains.stream().allMatch(s1 -> finalS.contains(s1))) {
                s2 = replace;
            }
            return s + "/" + s2;
        }).get();
    }
}
