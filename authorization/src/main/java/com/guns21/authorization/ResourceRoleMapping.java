package com.guns21.authorization;

import com.guns21.authorization.domain.AccessResource;

import java.util.List;

/**
 * 资源和角色的映射关系
 */
public interface ResourceRoleMapping {

    /**
     * 返回资源所对应的角色
     *
     * @param resource url资源
     * @param access  资源的访问方式
     * @return 返回资源所对应的角色,只能使用ArrayList封装
     */
    List<AccessResource> listRole(String resource, String access);
}
