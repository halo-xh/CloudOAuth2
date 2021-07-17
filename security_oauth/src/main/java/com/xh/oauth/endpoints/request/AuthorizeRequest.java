package com.xh.oauth.endpoints.request;

import lombok.Data;

import java.util.List;

/**
 * author  Xiao Hong
 * date  2021/7/17 16:13
 * description
 */
@Data
public class AuthorizeRequest {

    private List<String> grantAuthorities;

    private Boolean approve;

    private String exchangeCode;

}
