package com.xh.oauth.exception;

/**
 * author  Xiao Hong
 * date  2021/7/26 21:02
 * description
 */

public class ClientRegistrationException extends RuntimeException {

    public ClientRegistrationException(String msg) {
        super(msg);
    }

    public ClientRegistrationException(String msg, Throwable cause) {
        super(msg, cause);
    }

}

