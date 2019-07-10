package com.guns21.authentication.ext;

import com.guns21.authentication.api.service.AuthExtValidation;
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
    private StringBuffer errorBuffer = new StringBuffer();

    public boolean run(HttpServletRequest request) {
        boolean result = true;
        errorBuffer.delete(0, errorBuffer.length());

        for (AuthExtValidation validation : list) {
            if (validation != null && !validation.validate(request)) {
                errorBuffer.append(validation.getValidateError());
                result = false;
            }
        }

        return result;
    }

    public void addAuthExtValidation(AuthExtValidation validation) {
        this.list.add(validation);
    }

    public String getError() {
        return this.errorBuffer.toString();
    }
}
