package com.xh.oauth.security.authenticate;

import com.xh.oauth.exception.InvalidGrantException;

public interface AuthorizationCodeServices {

    /**
     * Create a authorization code for the specified authentications.
     *
     * @param authentication The authentications to store.
     * @return The generated code.
     */
    String createAuthorizationCode(Oauth2Authentication authentication);

    /**
     * Consume a authorization code.
     */
    Oauth2Authentication consumeAuthorizationCode(String code)
            throws InvalidGrantException;
}
