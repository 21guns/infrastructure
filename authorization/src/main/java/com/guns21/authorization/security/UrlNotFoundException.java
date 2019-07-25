package com.guns21.authorization.security;

import org.springframework.security.core.AuthenticationException;

public class UrlNotFoundException extends AuthenticationException {
    public UrlNotFoundException(String msg, Throwable t) {
        super(msg, t);
    }

    public UrlNotFoundException(String msg) {
        super(msg);
    }
}
