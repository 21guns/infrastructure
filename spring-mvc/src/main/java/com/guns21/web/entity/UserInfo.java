package com.guns21.web.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.*;
import java.util.List;

/**
 * 由于用户登录成功后返回给客户端
 * Created by ljj on 17/7/14.
 */
@Getter
@AllArgsConstructor
public class UserInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;

    private String name;

    private String nickname;

    private List<Role> roles;


}