package com.guns21.authentication.api.entity;

import com.guns21.user.login.domain.Role;
import lombok.Data;

import java.util.List;

/**
 * 用于接收从数据库中读取用户信息
 * Created by ljj on 2017/6/17.
 */
@Data
public class AuthUser {
    private String id;
    private String userName;
    private String nickName;
    private String password;
    private String passwordSalt;
    private String organizationId;
    private Boolean wxAccountBound;
    private List<Role> roles;

}
