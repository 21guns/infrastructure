package com.guns21.authentication.provider.service;

import com.guns21.authentication.api.entity.MyRole;
import com.guns21.authentication.api.entity.MyUser;
import com.guns21.authentication.api.entity.MyUserDetails;
import com.guns21.authentication.api.service.MySecurityAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by ljj on 2017/6/18.
 */

//@Service
public class MyUserDetailsService implements UserDetailsService {
    @Value("${com.ktjr.security.message.user-not-exist:用户不存在！}")
    private String userNotExistMessage;

    @Autowired
    private MySecurityAuthService mySecurityAuthService;

    public MyUserDetails loadUserByUsername(String username) {
        //从数据库中获取用户
        MyUser myUser = mySecurityAuthService.getUser(username);

        if (myUser == null) {
            throw new UsernameNotFoundException(userNotExistMessage);
        }

//        List<MyRole> roles = mySecurityAuthService.getUserRoles(username);

//        List<GrantedAuthority> grantedAuthorities = null;
//        if (roles != null) {
//            grantedAuthorities = roles.stream().map(r -> new SimpleGrantedAuthority(r.getName())).collect(Collectors.toList());
//        }

        MyUserDetails myUserDetails = new MyUserDetails(myUser.getUserName(), myUser.getPassword(), Collections.EMPTY_LIST);
        myUserDetails.setSalt(myUser.getSalt());
        myUserDetails.setUserId(myUser.getId());
        myUserDetails.setNickname(myUser.getNickname());
        myUserDetails.setOrganizationId(myUser.getOrganizationId());
//        myUserDetails.setRoles(roles);

        return myUserDetails;
    }

}