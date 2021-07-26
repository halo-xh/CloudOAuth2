package com.xh.oauth.exception;

/**
 * author  Xiao Hong
 * date  2021/7/26 21:46
 * description
 */

public class InvalidClientException extends OAuth2Exception {
    public InvalidClientException(String errorMessage) {
        super(errorMessage);
    }
}

