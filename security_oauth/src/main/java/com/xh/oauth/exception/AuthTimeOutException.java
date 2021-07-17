package com.xh.oauth.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * author  Xiao Hong
 * date  2021/7/17 16:24
 * description especially used after cached key time out.
 */
public class AuthTimeOutException extends AuthenticationException {


    public AuthTimeOutException(String msg) {
        super(msg);
    }

    public AuthTimeOutException(String msg, Throwable t) {
        super(msg, t);
    }
}
