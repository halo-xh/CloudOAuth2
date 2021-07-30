package com.xh.oauth.token.provider;

import com.xh.oauth.security.authenticate.Oauth2Request;

public interface ClientTokenProvider {

    boolean validateToken(String temTokenKey);

    Oauth2Request getPrincipal(String token);

    Long createToken(Oauth2Request oauth2Request);

    String getCached(Long reqId);
}
