package com.xh.oauth.security.provider;

import com.xh.oauth.security.authenticate.ClientAuthentication;
import com.xh.oauth.security.authenticate.Oauth2Authentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;


/**
 * author  Xiao Hong
 * date  2020/12/6 17:45
 * description
 */

public class UserAuthenticationProvider implements AuthenticationProvider {

    private static final Logger logger = LoggerFactory.getLogger(UserAuthenticationProvider.class);


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Oauth2Authentication oauth2Authentication = (Oauth2Authentication) authentication;
        // does not pass client authentication, fast fail.
        if (!oauth2Authentication.isClientAuthenticated()) {
            logger.info("does not pass client authentication, fast fail.");
            return null;
        }
        // haven't login(parse from jwt token), fast fail.
        Oauth2Authentication aNew = ((Oauth2Authentication) authentication).createNew();
        if (!oauth2Authentication.isUserAuthenticated()) {
            logger.info("user haven't login(parse from jwt token), fast fail.");
            return aNew;
        }
        // can get userAuthentication from jwt, start authenticate
        logger.info("can get user Authentication from jwt, can step in.");
        return aNew;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return Oauth2Authentication.class.isAssignableFrom(authentication);
    }

}
