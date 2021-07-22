package com.xh.oauth.token;

import com.xh.oauth.token.entity.OAuthClientToken;
import com.xh.oauth.token.repo.OAuthClientTokenRepository;
import com.xh.oauth.token.service.OAuthClientTokenService;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.ClientKeyGenerator;
import org.springframework.security.oauth2.client.token.ClientTokenServices;
import org.springframework.security.oauth2.client.token.DefaultClientKeyGenerator;
import org.springframework.security.oauth2.client.token.JdbcClientTokenServices;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.util.SerializationUtils;
import org.springframework.stereotype.Service;

/**
 * @author xiaohong
 * @version 1.0
 * @date 2021/7/15 16:32
 * <p>
 * stores tokens in a database for retrieval by client applications. 参考 {@link JdbcClientTokenServices}
 */
public class MyClientTokenService implements ClientTokenServices {

    private OAuthClientTokenService oAuthClientTokenService;

    private ClientKeyGenerator keyGenerator = new DefaultClientKeyGenerator();

    @Override
    public OAuth2AccessToken getAccessToken(OAuth2ProtectedResourceDetails resource, Authentication authentication) {
//        String authId = keyGenerator.extractKey(resource, authentication);
//        OAuthClientToken authClientToken = oAuthClientTokenService.findByAuthId(authId);
//        if (authClientToken == null) {
//            return null;
//        }
//        byte[] tokenAuthentication = authClientToken.getAuthentication();
//        return SerializationUtils.deserialize(tokenAuthentication);
        return null;

    }


    @Override
    public void saveAccessToken(OAuth2ProtectedResourceDetails resource, Authentication authentication, OAuth2AccessToken accessToken) {

    }

    @Override
    public void removeAccessToken(OAuth2ProtectedResourceDetails resource, Authentication authentication) {
    }


    public void setoAuthClientTokenService(OAuthClientTokenService oAuthClientTokenService) {
        this.oAuthClientTokenService = oAuthClientTokenService;
    }

    public void setKeyGenerator(ClientKeyGenerator keyGenerator) {
        this.keyGenerator = keyGenerator;
    }
}
