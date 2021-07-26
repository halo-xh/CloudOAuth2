package com.xh.oauth.exception;

/**
 * author  Xiao Hong
 * date  2021/7/26 21:44
 * description
 */
public abstract class ClientAuthenticationException extends OAuth2Exception {

    public ClientAuthenticationException(String msg, Throwable t) {
        super(msg, t);
    }

    public ClientAuthenticationException(String msg) {
        super(msg);
    }

    @Override
    public int getHttpErrorCode() {
        // The spec says this is a bad request (not unauthorized)
        return 400;
    }

    @Override
    public abstract String getOAuth2ErrorCode();
}
