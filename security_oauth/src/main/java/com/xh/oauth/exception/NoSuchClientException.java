package com.xh.oauth.exception;

/**
 * author  Xiao Hong
 * date  2021/7/26 21:02
 * description
 */
public class NoSuchClientException extends ClientRegistrationException {

    public NoSuchClientException(String msg) {
        super(msg);
    }

    public NoSuchClientException(String msg, Throwable cause) {
        super(msg, cause);
    }

}

