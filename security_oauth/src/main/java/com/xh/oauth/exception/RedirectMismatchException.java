package com.xh.oauth.exception;

/**
 * author  Xiao Hong
 * date  2021/7/26 21:48
 * description
 */

public class RedirectMismatchException extends OAuth2Exception {
    public RedirectMismatchException(String errorMessage) {
        super(errorMessage);
    }
}
