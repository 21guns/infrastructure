package com.guns21.authentication.mixin;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.guns21.authentication.api.entity.UserRoleDetails;
import com.guns21.web.entity.Role;
import com.guns21.web.entity.UserInfo;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class AuthenticationJasksonModue extends SimpleModule {

    public AuthenticationJasksonModue() {
        super(AuthenticationJasksonModue.class.getName(), new Version(1, 0, 0, (String) null, (String) null, (String) null));
    }

    @Override
    public void setupModule(SetupContext context) {
        context.setMixInAnnotations(UserRoleDetails.class, UserRoleDetailsMixin.class);
        context.setMixInAnnotations(UsernamePasswordAuthenticationToken.class, UsernamePasswordAuthenticationTokenMixin.class);
        context.setMixInAnnotations(Role.class, RoleMixin.class);
        context.setMixInAnnotations(UserInfo.class, RoleMixin.class);
    }
}

