package com.guns21.authorization.security;

import com.guns21.authentication.boot.config.SecurityConfig;
import com.guns21.authorization.ResourceRoleMapping;
import com.guns21.authorization.domain.AccessResource;
import com.guns21.common.util.ObjectUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class HttpAuthorizationManagerAdapter implements AuthorizationManager<RequestAuthorizationContext> {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpAuthorizationManagerAdapter.class);
    private static final String ROLE_ANONYMOUS = "ROLE_ANONYMOUS";
    private static final String SUPER_ADMIN = "SUPER_ADMIN";
    private final String permissionRedisKey = "permission_redis_key";

    @Resource(name = "redisTemplate")
    private RedisTemplate<String, Map<String, List<String>>> template;
    @Autowired
    private ResourceRoleMapping resourceRoleMapping;

    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    @Autowired
    private MessageSourceAccessor messageSourceAccessor;

    @Autowired
    private SecurityConfig securityConfig;


    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext requestAuthorizationContext) {
        try {
            Collection<ConfigAttribute> attributes = this.getAttributes(requestAuthorizationContext);
            this.decide(authentication.get(), attributes);
            return new AuthorizationDecision(true);
        } catch (AccessDeniedException ex) {
            return new AuthorizationDecision(false);
        }
    }

    public Collection<ConfigAttribute> getAttributes(RequestAuthorizationContext requestAuthorizationContext)
            throws IllegalArgumentException {
        HttpServletRequest request = requestAuthorizationContext.getRequest();
        List<String> roles =  Collections.emptyList();
        String requestURI = request.getRequestURI();
        String method = request.getMethod();

        String urlPattern = null ;
        Map<RequestMappingInfo, HandlerMethod> pathMatcher = requestMappingHandlerMapping.getHandlerMethods();
        try {
            HandlerExecutionChain handlerExecutionChain = requestMappingHandlerMapping.getHandler(request);
            urlPattern = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
            LOGGER.debug("urlPattern:{}",urlPattern);
//            if (Objects.nonNull(handlerExecutionChain)) {
//                HandlerMethod handlerMethod = (HandlerMethod) handlerExecutionChain.getHandler();
//                RequestMapping methodAnnotation = handlerMethod.getMethodAnnotation(RequestMapping.class);
//                String[] path = methodAnnotation.path();
//            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (Objects.nonNull(urlPattern)) {
            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                throw new AuthenticationCredentialsNotFoundException(
                        "An Authentication requestAuthorizationContext was not found in the SecurityContext");
            }

            String key = method + ":" + urlPattern;
            //取redis中的数据
            BoundHashOperations<String, String,  List<String>> ops = template.boundHashOps(permissionRedisKey);

            //TODO 添加过期时间
            roles = ops.get(key);
            if (ObjectUtils.isEmpty(roles)) {
                //  使用ant表达式 /api/usermanage/v1/org/{no:MD[0-9]{19}}/salesman --> /api/usermanage/v1/org/*/salesman
                String antUrl  = replaceBrace(urlPattern, "*", Arrays.asList("{","}"));
//                List<String> resourceList = matchingCondition.getPatternValues().stream()
//                        .map(s -> replaceBrace(s, "*", Arrays.asList("{", "}")))
//                        .toList();
                //提供数据来源
                List<AccessResource> accessResources = resourceRoleMapping.listRole(requestURI, method);
                if (Objects.nonNull(accessResources)) {
                    for (int i = 0; i < accessResources.size(); i++) {
                        AccessResource accessResource = accessResources.get(i);
                        //访问资源
                        if (AccessResource.FULL_RESOURCE.equals(accessResource.getPermissionUrl())
                                || antUrl.equals(accessResource.getPermissionUrl()) ) {
                            //比较访问方式
                            if (AccessResource.FULL_ACCESS.equals(accessResource.getRequestMethod())
                                    || method.equals(accessResource.getRequestMethod())) {
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
        /**
         * 如果对应的资源没有配置角色：
         * 1.返回空集合是白名单
         * 2.返回非空集合是黑名单,匿名访问时是黑名单
         */
        if (null == roles || roles.size() == 0) {
            LOGGER.warn("url {} hasn't roles and return SUPER_ADMIN's role", requestURI);
            if (securityConfig.isAnonymous()) {
                return Collections.singleton(new org.springframework.security.access.SecurityConfig(ROLE_ANONYMOUS));
            } else {
                return Collections.singleton(new org.springframework.security.access.SecurityConfig(SUPER_ADMIN));
            }
        }

        return roles.stream().map(org.springframework.security.access.SecurityConfig::new)
                .collect(Collectors.toList());

    }


    /**
     * 判断configAttribute中找角色在authentication中是否存在，如果不存在throws 异常。
     */
    private   void decide(Authentication authentication, final Collection<ConfigAttribute> configAttributes) {

        /**
         * 1.如果url对应的角色列表为空，禁止访问
         */
        if (null == configAttributes || configAttributes.size() == 0) {
            throw new AccessDeniedException(messageSourceAccessor.getMessage("com.guns21.security.message.access.denied","没有访问权限"));
        }

        /**
         * 2.启用匿名访问时，检查角色列表是否包含匿名用户
         */
        if (securityConfig.isAnonymous() && configAttributes.stream().map(ca -> ca.getAttribute()).anyMatch(role -> role.equals(ROLE_ANONYMOUS))) {
            return;
        }

        /**
         * 3.没有授权用户，并且授权用户没有权限
         */
        if (null == authentication || authentication.getAuthorities() == null || authentication.getAuthorities().size() == 0) {
            throw new AccessDeniedException(messageSourceAccessor.getMessage("com.guns21.security.message.access.denied","没有访问权限"));
        }

        /**
         * 4.用户权限检查
         *
         */
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
        throw new AccessDeniedException(messageSourceAccessor.getMessage("com.guns21.security.message.access.denied","没有访问权限"));
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