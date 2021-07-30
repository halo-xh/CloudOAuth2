package com.xh.oauth.security.filters;

import com.xh.oauth.security.authenticate.ClientAuthentication;
import com.xh.oauth.security.authenticate.Oauth2Authentication;
import com.xh.oauth.security.authenticate.Oauth2Request;
import com.xh.oauth.token.ClientTokenProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;


/**
 * Created by Xiao Hong on 2021-07-22
 * </p>
 * extract and validate client authentication token from request header.
 */
@Component
public class ClientTokenFilter extends OncePerRequestFilter {

    public static final String CLIENT_SECRET = "client_secret";
    private ClientTokenProvider clientTokenProvider;

    public static final String BEARER_ = "Bearer ";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = resolveToken(request);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        if (StringUtils.hasText(token) && clientTokenProvider.validateToken(token)) {
            Oauth2Request principal = clientTokenProvider.getPrincipal(token);
            ClientAuthentication clientAuthentication = new ClientAuthentication(principal, null);
            Oauth2Authentication oauth2Authentication = new Oauth2Authentication(clientAuthentication, null);
            securityContext.setAuthentication(oauth2Authentication);
        } else {
            logger.info("client validate failed.. may token time out. try create authentication from request info.");
            //try create authentication from request info. help later authentication provider
            ClientAuthentication clientAuthentication = attemptBuildClientAuthenticate(request);
            SecurityContextHolder.clearContext();
            if (clientAuthentication != null) {
                securityContext.setAuthentication(clientAuthentication);
                logger.info("client authenticated from url parameters.");
            }
        }
        filterChain.doFilter(request, response);
    }

    private ClientAuthentication attemptBuildClientAuthenticate(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        try {
            Oauth2Request oauth2Request = buildAuthRequest(parameterMap);
            return new ClientAuthentication(oauth2Request, parameterMap.get(CLIENT_SECRET)[0]);
        } catch (Exception e) {
            logger.error("create client authentication failed..");
        }
        return null;
    }

    private Oauth2Request buildAuthRequest(Map<String, String[]> parameters) {
        Oauth2Request oauth2Request = new Oauth2Request();
        oauth2Request.setClientId(parameters.get(OAuth2Utils.CLIENT_ID)[0]);
        oauth2Request.setRedirectUri(parameters.get(OAuth2Utils.REDIRECT_URI)[0]);
        oauth2Request.setResponseType(parameters.get(OAuth2Utils.RESPONSE_TYPE)[0]);
//        oauth2Request.setState(parameters.get(OAuth2Utils.STATE)[0]);
        oauth2Request.setScope(parameters.get(OAuth2Utils.SCOPE)[0]);
        return oauth2Request;
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.WWW_AUTHENTICATE);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_)) {
            return bearerToken;
        }
        return null;
    }

    public void setClientTokenProvider(ClientTokenProvider clientTokenProvider) {
        this.clientTokenProvider = clientTokenProvider;
    }
}
