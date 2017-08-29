package com.ktjr.security.api.entity;

import java.io.*;

/**
 * Created by ljj on 17/7/14.
 */
public class MyRole implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private String nickname;

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
}
