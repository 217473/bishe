package com.itmk.config.security.exception;

import org.springframework.security.core.AuthenticationException;

public class CustomerAuthenionException extends AuthenticationException {
    /**
     * 自定义异常
     * @param msg
     */
    public CustomerAuthenionException(String msg) {
        super(msg);
    }
}
