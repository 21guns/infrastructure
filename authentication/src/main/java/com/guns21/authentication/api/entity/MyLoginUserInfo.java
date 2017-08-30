package com.guns21.authentication.api.entity;

import java.io.*;
import java.util.List;

/**
 * 由于用户登录成功后返回给客户端
 * Created by ljj on 17/7/14.
 */
public class MyLoginUserInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;

    private String name;

    private String nickname;

    private List<MyRole> roles;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public List<MyRole> getRoles() {
        return roles;
    }

    public void setRoles(List<MyRole> roles) {
        this.roles = roles;
    }
}
