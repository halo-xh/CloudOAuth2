package com.xh.oauth.security.provider;

import com.xh.oauth.clients.entity.ClientDetails;
import com.xh.oauth.clients.service.ClientDetailsService;
import com.xh.oauth.security.authenticate.ClientAuthentication;
import com.xh.oauth.security.authenticate.Oauth2Request;
import com.xh.oauth.token.ClientTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Created by Xiao Hong on 2021-07-22
 * </p>
 */
public class ClientAuthenticationProvider implements AuthenticationProvider {

    private static final Logger logger = LoggerFactory.getLogger(ClientAuthenticationProvider.class);

    private ClientDetailsService clientDetailsService;

    private PasswordEncoder passwordEncoder;

    private ClientTokenProvider clientTokenProvider;
    /**
     * The password used to perform
     * {@link PasswordEncoder#matches(CharSequence, String)} on when the user is
     * not found to avoid SEC-2056. This is necessary, because some
     * {@link PasswordEncoder} implementations will short circuit if the password is not
     * in a valid format.
     */
    private volatile String userNotFoundEncodedPassword;

    /**
     * The plaintext password used to perform
     * PasswordEncoder#matches(CharSequence, String)}  on when the user is
     * not found to avoid SEC-2056.
     */
    private static final String USER_NOT_FOUND_PASSWORD = "userNotFoundPassword";

    private final MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Oauth2Request principal = (Oauth2Request) authentication.getPrincipal();
        Object credentials = authentication.getCredentials();
        ClientDetails clientDetails = retrieveClient(principal.getClientId(), authentication);
        if (!matchCredentials(credentials, clientDetails)) {
            throw new BadCredentialsException(messages.getMessage(
                    "ClientAuthenticationProvider.badCredentials", "Bad credentials"));
        }
        Long tokenId = clientTokenProvider.createToken(principal);
        principal.setRequestId(tokenId);
        logger.info("passed client authenticate...");
        return createSuccessAuthentication(principal);
    }

    private boolean matchCredentials(Object credentials, ClientDetails clientDetails) {
        if (credentials == null) {
            throw new BadCredentialsException(messages.getMessage(
                    "ClientAuthenticationProvider.badCredentials", "Bad credentials, null"));
        }

        CharSequence presentedPassword = (String) credentials;

        if (!passwordEncoder.matches(presentedPassword, clientDetails.getClientSecret())) {
            logger.debug("Authentication failed: password does not match stored value");

            throw new BadCredentialsException(messages.getMessage(
                    "ClientAuthenticationProvider.badCredentials", "Bad credentials"));
        }
        return true;
    }


    private ClientAuthentication createSuccessAuthentication(Oauth2Request principal) {
        return new ClientAuthentication(principal, null);
    }

    private ClientDetails retrieveClient(String clientId, Authentication authentication) throws AuthenticationException {
        prepareTimingAttackProtection();
        try {
            ClientDetails client = this.clientDetailsService.loadClientByClientId(clientId);
            if (client == null) {
                throw new InternalAuthenticationServiceException(
                        "UserDetailsService returned null, which is an interface contract violation");
            }
            return client;
        } catch (UsernameNotFoundException ex) {
            mitigateAgainstTimingAttack(authentication);
            throw ex;
        } catch (InternalAuthenticationServiceException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex);
        }
    }

    private void prepareTimingAttackProtection() {
        if (this.userNotFoundEncodedPassword == null) {
            this.userNotFoundEncodedPassword = this.passwordEncoder.encode(USER_NOT_FOUND_PASSWORD);
        }
    }

    private void mitigateAgainstTimingAttack(Authentication authentication) {
        if (authentication.getCredentials() != null) {
            String presentedPassword = authentication.getCredentials().toString();
            this.passwordEncoder.matches(presentedPassword, this.userNotFoundEncodedPassword);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return ClientAuthentication.class.isAssignableFrom(authentication);
    }

    public void setClientDetailsService(ClientDetailsService clientDetailsService) {
        this.clientDetailsService = clientDetailsService;
    }

    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public void setClientTokenProvider(ClientTokenProvider clientTokenProvider) {
        this.clientTokenProvider = clientTokenProvider;
    }
}
