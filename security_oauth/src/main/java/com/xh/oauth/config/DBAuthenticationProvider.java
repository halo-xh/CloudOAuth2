package com.xh.oauth.config;

import com.xh.oauth.user.LoginUser;
import com.xh.oauth.user.entity.Subject;
import com.xh.oauth.web.AuthorityService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * author  Xiao Hong
 * date  2020/12/6 17:45
 * description
 */

public class DBAuthenticationProvider extends DaoAuthenticationProvider {

    private AuthorityService authorityService;

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        this.logger.info("additionalAuthenticationChecks()");
        if (authentication.getCredentials() == null) {
            this.logger.debug("Authentication failed: no credentials provided");
            throw new BadCredentialsException(this.messages.getMessage(
                    "AbstractUserDetailsAuthenticationProvider.badCredentials",
                    "Bad credentials"));
        }
        String presentedPassword = (String) authentication.getCredentials();
        if (getPasswordEncoder().matches(presentedPassword, userDetails.getPassword())) {
            // may expire check.todo
            Collection<GrantedAuthority> authorities = authorityService.getGrantedAuthorityByLoginName(((LoginUser) userDetails).getLoginname());
            ((LoginUser) userDetails).setAuthorities(authorities);
            return;
        }
        this.logger.debug("Authentication failed: password does not match stored value");
        throw new BadCredentialsException(this.messages.getMessage(
                "AbstractUserDetailsAuthenticationProvider.badCredentials",
                "Bad credentials"));
    }

    public void setAuthorityService(AuthorityService authorityService) {
        this.authorityService = authorityService;
    }

}
