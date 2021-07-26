package com.xh.oauth.exception;

/**
 * author  Xiao Hong
 * date  2021/7/26 21:47
 * description
 */

public class UnauthorizedClientException extends OAuth2Exception {
    public UnauthorizedClientException(String errorMessage) {
        super(errorMessage);
    }
}
