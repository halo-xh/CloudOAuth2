package com.xh.oauth.security.authenticate;

import org.springframework.security.authentication.AbstractAuthenticationToken;


/**
 * @author xiaohong
 * @date 2021/7/22 10:37
 */
public class ClientAuthentication extends AbstractAuthenticationToken {

    /**
     * client request. identified by a unique id, set when build the authentication.
     */
    private final Oauth2Request principal;

    /**
     * client secret
     */
    private final String credentials;


    public ClientAuthentication(Oauth2Request principal, String credentials) {
        super(null);
        this.credentials = credentials;
        this.principal = principal;
    }

    @Override
    public Object getCredentials() {
        return credentials;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

}
