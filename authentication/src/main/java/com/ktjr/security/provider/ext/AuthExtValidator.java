package com.ktjr.security.provider.ext;

import com.ktjr.security.api.service.AuthExtValidation;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ljj on 17/8/15.
 */
@Component
public class AuthExtValidator {
    private List<AuthExtValidation> list = new ArrayList<>();
    private StringBuilder errorBuilder = new StringBuilder();

    public boolean run(HttpServletRequest request) {
        boolean result = true;
        errorBuilder.delete(0, errorBuilder.length());

        for (AuthExtValidation validation : list) {
            if (validation != null && !validation.validate(request)) {
                errorBuilder.append(validation.getValidateError());
                result = false;
            }
        }

        return result;
    }

    public void addAuthExtValidation(AuthExtValidation validation) {
        this.list.add(validation);
    }

    public String getError() {
        return this.errorBuilder.toString();
    }
}
