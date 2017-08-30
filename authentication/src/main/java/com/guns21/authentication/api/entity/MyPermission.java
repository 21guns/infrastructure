package com.guns21.authentication.api.entity;

/**
 * Created by ljj on 2017/6/18.
 */

public class MyPermission {
    private String role;
    private String permissionUrl;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPermissionUrl() {
        return permissionUrl;
    }

    public void setPermissionUrl(String permissionUrl) {
        this.permissionUrl = permissionUrl;
    }
}
