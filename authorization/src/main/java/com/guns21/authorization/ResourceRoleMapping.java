package com.guns21.authorization;

import java.util.List;

/**
 * 资源和角色的映射关系
 */
public interface ResourceRoleMapping {

    /**
     * 返回资源所对应的角色
     *
     * @param resource url资源
     * @return 返回资源所对应的角色
     */
    List<String> listRole(String resource);
}
