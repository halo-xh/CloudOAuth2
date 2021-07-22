package com.xh.oauth.token;

import com.xh.oauth.security.authenticate.Oauth2Request;

public interface ClientTokenProvider {

    boolean validateToken(String temTokenKey);

    Oauth2Request getPrincipal(String token);

    Long createToken(Oauth2Request oauth2Request);
}
