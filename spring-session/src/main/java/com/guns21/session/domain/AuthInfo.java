package com.guns21.session.domain;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ljj on 17/5/24.
 */
public class AuthInfo implements Serializable {
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    private String userId;
    private String userName;
    private List<String> roles;

}

