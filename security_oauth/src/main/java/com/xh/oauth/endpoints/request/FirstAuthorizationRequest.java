package com.xh.oauth.endpoints.request;

import lombok.Data;

/**
 * author  Xiao Hong
 * date  2021/7/17 15:12
 * description first authorize request parameters.
 * used to remember temporary.
 */
@Data
public class FirstAuthorizationRequest {

    private String clientId;

    private String responseType;

    private String redirectUri;

    private String state;

}
