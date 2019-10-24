package com.guns21.authentication.api.service;

/**
 * Created by ljj on 17/6/28.
 */

import com.guns21.authentication.api.entity.AuthUser;
import com.guns21.user.login.domain.Role;

import java.util.List;

/**
 * 用于用户的登录处理
 * 提供两个需要子类实现的方法getUser、getUserRoles，
 * Created by ljj on 17/6/28.
 */
public interface UserAuthService {

    /**
     * 登录时根据用户登录名称获取AuthUser类型的用户信息（需要子类实现）
     *
     * @param username 用户名
     * @return
     */
    AuthUser getUser(String username);

    /**
     * 登录时，根据用户登录名称获取用户的角色列表（需要子类实现）
     *
     * @param username 用户名
     * @return 只能使用ArrayList封装
     */
    List<Role> getUserRoles(String username);
}
