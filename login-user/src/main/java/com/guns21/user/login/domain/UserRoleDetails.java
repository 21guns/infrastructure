package com.guns21.user.login.domain;


import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.List;

/**
 * Created by ljj on 17/6/28.
 */
@Setter
@Getter
public class UserRoleDetails extends User {
    private static final long serialVersionUID = 1L;

    private String passwordSalt;
    private String userId;
    private String nickname;
    private String organizationId;
    private Boolean wxAccountBound;
    private List<Role> roles;


    public UserRoleDetails(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        this(username, password, true, true, true, true, authorities);
    }

    public UserRoleDetails(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    }
}
