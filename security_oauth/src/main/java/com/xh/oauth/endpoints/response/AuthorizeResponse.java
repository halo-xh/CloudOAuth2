package com.xh.oauth.endpoints.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.SpringSecurityCoreVersion;

import java.util.List;

/**
 * author  Xiao Hong
 * date  2021/7/17 14:22
 * description
 */
@Data
public class AuthorizeResponse {

    /**
     * 如果用户已经登录 则true，前端直接跳转是否授权页面
     */
    private Boolean authenticated = false;

    /**
     * 用户未登录，返回登录校验code. 实则是保存上次验证通过client 的key
     */
    private String exchangeCode;

    /**
     * 用户可以授权的选择范围(开放给client的权限列表)
     */
    private List<String> authorities;

    // ============= 默认直接授权==============
    /**
     * 是否已经同意授权。 用于在默认直接授权的情况。
     * 如果为true，前端应该直接拼接
     */
    private Boolean approved = false;

    /**
     * 授权码。用于换取真正的token
     */
    private String authorizeCode;

    /**
     * redirect url
     */
    private String redirectUrl;

    public AuthorizeResponse() {
    }

    public AuthorizeResponse(Boolean authenticated, String exchangeCode, Boolean approved) {
        this.authenticated = authenticated;
        this.exchangeCode = exchangeCode;
        this.approved = approved;
    }
}
