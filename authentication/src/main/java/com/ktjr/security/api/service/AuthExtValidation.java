package com.ktjr.security.api.service;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by ljj on 17/8/15.
 */
public interface AuthExtValidation {
    boolean validate(HttpServletRequest request);

    String getValidateError();
}
