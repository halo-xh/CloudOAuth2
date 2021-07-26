package com.xh.oauth.endpoints.request;

import com.xh.oauth.clients.entity.ClientDetails;
import com.xh.oauth.exception.InvalidClientException;
import com.xh.oauth.exception.InvalidRequestException;
import com.xh.oauth.exception.UnsupportedResponseTypeException;
import com.xh.oauth.security.filters.OAuth2Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Strategy for managing OAuth2 requests
 */
public class OAuth2RequestFactory {

    private final Logger logger = LoggerFactory.getLogger(OAuth2RequestFactory.class);

    public AuthorizationRequest createAuthorizationRequest(Map<String, String> authorizationParameters) {
        AuthorizationRequest authorizationRequest = null;
        try {
            authorizationRequest = buildFirstAuthRequest(authorizationParameters);
        } catch (Exception exception) {
            logger.error("invalid authorize request . ");
            exception.printStackTrace();
            throw new InvalidRequestException("invalid request");
        }
        // 获取response_type
        Set<String> responseTypes = authorizationRequest.getResponseType();
        // check
        //获取response_type
        if (!responseTypes.contains("token") && !responseTypes.contains("code")) {
            throw new UnsupportedResponseTypeException("Unsupported response types: " + responseTypes);
        }
        // client id
        if (authorizationRequest.getClientId() == null) {
            throw new InvalidClientException("A client id must be provided");
        }
        return authorizationRequest;
    }

    private AuthorizationRequest buildFirstAuthRequest(Map<String, String> parameters) {
        AuthorizationRequest authorizationRequest = new AuthorizationRequest();
        authorizationRequest.setClientId(parameters.get(OAuth2Utils.CLIENT_ID));
        authorizationRequest.setRedirectUri(parameters.get(OAuth2Utils.REDIRECT_URI));
        String s = parameters.get(OAuth2Utils.RESPONSE_TYPE);
        String[] split = s.split(",");
        authorizationRequest.setResponseType(new HashSet<>(Arrays.asList(split)));
        authorizationRequest.setState(parameters.get(OAuth2Utils.STATE));
        authorizationRequest.setScope(parameters.get(OAuth2Utils.SCOPE));
        return authorizationRequest;
    }


    ///====================================================

    public OAuth2Request createOAuth2Request(AuthorizationRequest request) {
        return null;
    }

    public OAuth2Request createOAuth2Request(ClientDetails client, TokenRequest tokenRequest) {
        return null;
    }

    public TokenRequest createTokenRequest(Map<String, String> requestParameters, ClientDetails authenticatedClient) {
        return null;
    }

    public TokenRequest createTokenRequest(AuthorizationRequest authorizationRequest, String grantType) {
        return null;
    }

}
