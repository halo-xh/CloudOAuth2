package com.xh.oauth.exception;

/**
 * author  Xiao Hong
 * date  2021/7/26 21:44
 * description
 */

public class InvalidTokenException extends ClientAuthenticationException {

    public InvalidTokenException(String msg, Throwable t) {
        super(msg, t);
    }

    public InvalidTokenException(String msg) {
        super(msg);
    }

    @Override
    public int getHttpErrorCode() {
        return 401;
    }

    @Override
    public String getOAuth2ErrorCode() {
        return "invalid_token";
    }
}
