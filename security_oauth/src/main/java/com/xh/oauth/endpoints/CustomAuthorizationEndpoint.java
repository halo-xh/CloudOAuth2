package com.xh.oauth.endpoints;

import com.xh.oauth.endpoints.request.AuthorizeRequest;
import com.xh.oauth.endpoints.request.FirstAuthorizationRequest;
import com.xh.oauth.endpoints.response.AuthorizeResponse;
import com.xh.oauth.exception.AuthTimeOutException;
import com.xh.oauth.token.ClientTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.exceptions.InvalidClientException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.common.exceptions.UnsupportedResponseTypeException;
import org.springframework.security.oauth2.common.util.OAuth2Utils;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.approval.DefaultUserApprovalHandler;
import org.springframework.security.oauth2.provider.approval.UserApprovalHandler;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.endpoint.FrameworkEndpoint;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestValidator;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * author  Xiao Hong
 * date  2021/7/17 12:44
 * description 授权码模式控制器 参考:{@link org.springframework.security.oauth2.provider.endpoint.AuthorizationEndpoint}
 */
@RequestMapping("/oauth2")
@RestController
public class CustomAuthorizationEndpoint{

    private static final Logger logger = LoggerFactory.getLogger(CustomAuthorizationEndpoint.class);

    private OAuth2RequestValidator oauth2RequestValidator = new DefaultOAuth2RequestValidator();

    private UserApprovalHandler userApprovalHandler = new DefaultUserApprovalHandler();

    private final ClientTokenProvider clientTokenProvider;

    private final AuthorizationCodeServices authorizationCodeServices;

    private final OAuth2RequestFactory oAuth2RequestFactory;

    private final ClientDetailsService clientDetailsService;

    public CustomAuthorizationEndpoint(ClientTokenProvider clientTokenProvider,
                                       AuthorizationCodeServices authorizationCodeServices,
                                       OAuth2RequestFactory oAuth2RequestFactory,
                                       ClientDetailsService clientDetailsService) {
        this.clientTokenProvider = clientTokenProvider;
        this.authorizationCodeServices = authorizationCodeServices;
        this.oAuth2RequestFactory = oAuth2RequestFactory;
        this.clientDetailsService = clientDetailsService;
    }


    @GetMapping(value = "/authorize")
    public AuthorizeResponse authorize(@RequestParam Map<String, String> parameters) {
        // 构建参数
        AuthorizationRequest authorizationRequest = oAuth2RequestFactory.createAuthorizationRequest(parameters);
        // 获取response_type
        Set<String> responseTypes = authorizationRequest.getResponseTypes();
        // check
        //获取response_type
        if (!responseTypes.contains("token") && !responseTypes.contains("code")) {
            throw new UnsupportedResponseTypeException("Unsupported response types: " + responseTypes);
        }
        // client id
        if (authorizationRequest.getClientId() == null) {
            throw new InvalidClientException("A client id must be provided");
        }
        // 尝试获取 Authentication 判断是不是要进行验证
        Authentication authentication = null;
        try {
            authentication = SecurityContextHolder.getContext().getAuthentication();
        } catch (Exception e) {
            logger.error("cannot get authentication.user haven't login...");
        }
        ClientDetails client = clientDetailsService.loadClientByClientId(authorizationRequest.getClientId());
        // We intentionally only validate the parameters requested by the client (ignoring any data that may have
        // been added to the request by the manager).
        oauth2RequestValidator.validateScope(authorizationRequest, client);
        // 保存此次请求信息
        String storedKey = storeAuthRequest(parameters);

        /* ====not login==== */
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return new AuthorizeResponse(false, storedKey, false);
        }

        /* ====already authenticated==== */

        // Some systems may allow for approval decisions to be remembered or approved by default. Check for
        // such logic here, and set the approved flag on the authorization request accordingly.
        authorizationRequest = userApprovalHandler.checkForPreApproval(authorizationRequest, authentication);
        // TODO: is this call necessary?
        boolean approved = userApprovalHandler.isApproved(authorizationRequest, authentication);
        authorizationRequest.setApproved(approved);
        // Validation is all done, so we can check for auto approval...
        if (authorizationRequest.isApproved()) {
            if (responseTypes.contains("token")) {
                // return getImplicitGrantResponse(authorizationRequest);
            }
            // 授权码模式 - 默认授权
            if (responseTypes.contains("code")) {
                AuthorizeResponse authorizeResponse = new AuthorizeResponse(true, null, true);
                String generateCode = generateCode(authorizationRequest, authentication);
                authorizeResponse.setAuthorizeCode(generateCode);
                authorizeResponse.setRedirectUrl(parameters.get(OAuth2Utils.REDIRECT_URI));
                return authorizeResponse;
            }
        }
        AuthorizeResponse authorizeResponse = new AuthorizeResponse(true, storedKey, false);
        authorizeResponse.setAuthorities(getAuthorities(client));
        return authorizeResponse;
    }

    /**
     * 授权操作. 这个时候要拿着  echangecode(代表通过了上一步的client 验证) 进行操作。
     */
    @PostMapping(value = "/authorizer")
    public AuthorizeResponse authorize(@RequestBody AuthorizeRequest authorizeRequest) {
        String exchangeCode = authorizeRequest.getExchangeCode();
        if (!StringUtils.hasText(exchangeCode)) {
            throw new IllegalArgumentException("please provide exchange key.");
        }
        FirstAuthorizationRequest firstAuthorizationRequest = clientTokenProvider.validateToken(exchangeCode);
        if (firstAuthorizationRequest == null) {
            throw new AuthTimeOutException("authorize time out. please retry from original web side.");
        }
        Authentication authentication = null;
        try {
            authentication = SecurityContextHolder.getContext().getAuthentication();
        } catch (Exception e) {
            logger.error("cannot get authentication.user haven't login...");
        }
        if (authentication == null) {
            throw new AuthenticationCredentialsNotFoundException("login status time out, please retry.");
        }
        AuthorizationRequest authorizationRequest = new AuthorizationRequest();
        // set val todo.
        authorizationRequest.setApproved(true);
        List<SimpleGrantedAuthority> grantedAuthorities = authorizeRequest.getGrantAuthorities().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        authorizationRequest.setAuthorities(grantedAuthorities);
        authorizationRequest.setClientId(firstAuthorizationRequest.getClientId());
        authorizationRequest.setRedirectUri(firstAuthorizationRequest.getRedirectUri());
        if ("code".equals(firstAuthorizationRequest.getResponseType())) {
            AuthorizeResponse authorizeResponse = new AuthorizeResponse(true, null, true);
            String generateCode = generateCode(authorizationRequest, authentication);
            authorizeResponse.setAuthorizeCode(generateCode);
            authorizeResponse.setRedirectUrl(firstAuthorizationRequest.getRedirectUri());
            return authorizeResponse;
        }
        //todo. here 07-17
        return null;
    }


    private String storeAuthRequest(Map<String, String> parameters) {
        FirstAuthorizationRequest firstAuthorizationRequest = buildFirstAuthRequest(parameters);
        return clientTokenProvider.storeToken(firstAuthorizationRequest);
    }

    private FirstAuthorizationRequest buildFirstAuthRequest(Map<String, String> parameters) {
        FirstAuthorizationRequest firstAuthorizationRequest = new FirstAuthorizationRequest();
        firstAuthorizationRequest.setClientId(parameters.get(OAuth2Utils.CLIENT_ID));
        firstAuthorizationRequest.setRedirectUri(parameters.get(OAuth2Utils.REDIRECT_URI));
        firstAuthorizationRequest.setResponseType(parameters.get(OAuth2Utils.RESPONSE_TYPE));
        firstAuthorizationRequest.setState(parameters.get(OAuth2Utils.STATE));
        firstAuthorizationRequest.setScope(parameters.get(OAuth2Utils.SCOPE));
        return firstAuthorizationRequest;
    }


    private String generateCode(AuthorizationRequest authorizationRequest, Authentication authentication)
            throws AuthenticationException {
        try {
            OAuth2Request storedOAuth2Request = oAuth2RequestFactory.createOAuth2Request(authorizationRequest);
            OAuth2Authentication combinedAuth = new OAuth2Authentication(storedOAuth2Request, authentication);
            return authorizationCodeServices.createAuthorizationCode(combinedAuth);
        } catch (OAuth2Exception e) {
            if (authorizationRequest.getState() != null) {
                e.addAdditionalInformation("state", authorizationRequest.getState());
            }
            throw e;
        }
    }

    private List<String> getAuthorities(ClientDetails clientDetails) {
        Collection<GrantedAuthority> authorities = clientDetails.getAuthorities();
        ArrayList<String> arrayList = new ArrayList<>(authorities.size());
        for (GrantedAuthority authority : authorities) {
            String s = authority.getAuthority();
            arrayList.add(s);
        }
        return arrayList;
    }

}
