package com.xh.oauth.security.authenticate;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;

/**
 * @author xiaohong
 * @version 1.0
 * @date 2021/7/22 10:28
 * @description
 */
public class Oauth2Authentication extends AbstractAuthenticationToken {

    private final ClientAuthentication clientAuthentication;

    private Authentication userAuthentication;


    public Oauth2Authentication(ClientAuthentication clientAuthentication, Authentication userAuthentication) {
        super(null);
        this.clientAuthentication = clientAuthentication;
        this.userAuthentication = userAuthentication;
    }

    @Override
    public Object getCredentials() {
        return clientAuthentication.getCredentials();
    }

    @Override
    public Object getPrincipal() {
        return clientAuthentication.getPrincipal();
    }

    public boolean isUserAuthenticated() {
        return this.userAuthentication != null;
    }

    public boolean isClientAuthenticated() {
        return this.clientAuthentication != null;
    }

    public ClientAuthentication getClientAuthentication() {
        return clientAuthentication;
    }

    public Authentication getUserAuthentication() {
        return userAuthentication;
    }

    public void setUserAuthentication(Authentication userAuthentication) {
        this.userAuthentication = userAuthentication;
    }

    public Oauth2Authentication createNew() {
        return new Oauth2Authentication(this.getClientAuthentication(), this.getUserAuthentication());
    }

}
