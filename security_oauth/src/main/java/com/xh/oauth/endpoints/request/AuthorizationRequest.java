package com.xh.oauth.endpoints.request;

import lombok.Data;

import java.util.Set;

/**
 * author  Xiao Hong
 * date  2021/7/17 15:12
 * description first authorize request parameters.
 * used to remember temporary.
 */
@Data
public class AuthorizationRequest {

    private String clientId;

    private Set<String> responseType;

    private String redirectUri;

    private String state;

    private String scope;

}
