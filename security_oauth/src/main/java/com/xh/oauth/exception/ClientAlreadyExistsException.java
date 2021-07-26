package com.xh.oauth.exception;

/**
 * author  Xiao Hong
 * date  2021/7/26 21:01
 * description
 */
public class ClientAlreadyExistsException extends ClientRegistrationException {

    public ClientAlreadyExistsException(String msg) {
        super(msg);
    }

    public ClientAlreadyExistsException(String msg, Throwable cause) {
        super(msg, cause);
    }

}