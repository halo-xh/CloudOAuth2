package com.xh.oauth.exception;

/**
 * author  Xiao Hong
 * date  2021/7/26 21:48
 * description
 */

public class UnsupportedGrantTypeException extends OAuth2Exception {
    public UnsupportedGrantTypeException(String errorMessage) {
        super(errorMessage);
    }
}
