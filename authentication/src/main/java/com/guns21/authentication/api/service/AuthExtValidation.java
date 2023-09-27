package com.guns21.authentication.api.service;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Created by ljj on 17/8/15.
 */
public interface AuthExtValidation {
    boolean validate(HttpServletRequest request);

    String getValidateError();
}
