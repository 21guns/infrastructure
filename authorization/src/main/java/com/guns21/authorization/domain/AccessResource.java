package com.guns21.authorization.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Data
@AllArgsConstructor
public class AccessResource implements Serializable {

    /**
     * 资源
     */
    private String resource;

    /**
     * 访问方式
     */
    private String access;

    /**
     * 资源对应的角色名称
     */
    private String roleName;


    public List<String> getRole() {
        if (Objects.nonNull(roleName) && roleName.trim().length() > 0) {
            return new ArrayList(Arrays.asList(roleName.split(",")));
        }
        return null;
    }
}
