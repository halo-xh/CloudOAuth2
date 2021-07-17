package com.xh.oauth.endpoints;

import com.xh.oauth.endpoints.request.AuthorizeRequest;
import com.xh.oauth.endpoints.request.FirstAuthorizationRequest;
import com.xh.oauth.endpoints.response.AuthorizeResponse;
import com.xh.oauth.exception.AuthTimeOutException;
import com.xh.oauth.token.ClientTokenProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.exceptions.InvalidClientException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.common.exceptions.UnsupportedResponseTypeException;
import org.springframework.security.oauth2.common.util.OAuth2Utils;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.approval.DefaultUserApprovalHandler;
import org.springframework.security.oauth2.provider.approval.UserApprovalHandler;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.endpoint.AbstractEndpoint;
import org.springframework.security.oauth2.provider.endpoint.FrameworkEndpoint;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestValidator;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

/**
 * author  Xiao Hong
 * date  2021/7/17 12:44
 * description 授权码模式控制器 参考:{@link org.springframework.security.oauth2.provider.endpoint.AuthorizationEndpoint}
 */
@FrameworkEndpoint
public class AuthorizationEndpoint extends AbstractEndpoint {

    private OAuth2RequestValidator oauth2RequestValidator = new DefaultOAuth2RequestValidator();

    private UserApprovalHandler userApprovalHandler = new DefaultUserApprovalHandler();

    private ClientTokenProvider clientTokenProvider;

    private AuthorizationCodeServices authorizationCodeServices;


    @GetMapping(value = "/oauth2/authorize")
    public AuthorizeResponse authorize(@RequestParam Map<String, String> parameters) {

        // Pull out the authorization request first, using the OAuth2RequestFactory. All further logic should
        // query off of the authorization request instead of referring back to the parameters map. The contents of the
        // parameters map will be stored without change in the AuthorizationRequest object once it is created.
        // 构建参数
        AuthorizationRequest authorizationRequest = getOAuth2RequestFactory().createAuthorizationRequest(parameters);

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
        try {
            ClientDetails client = getClientDetailsService().loadClientByClientId(authorizationRequest.getClientId());
            // We intentionally only validate the parameters requested by the client (ignoring any data that may have
            // been added to the request by the manager).
            oauth2RequestValidator.validateScope(authorizationRequest, client);
            // 保存此次请求信息
            String storedKey = storeAuthRequest(parameters);
            /* ====not login==== */
            if (authentication == null) {
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
                    authorizeResponse.setAuthorizeCode(generateCode(authorizationRequest, authentication));
                    authorizeResponse.setRedirectUrl(parameters.get(OAuth2Utils.REDIRECT_URI));
                    return authorizeResponse;
                }
            }
            // todo: read cache from redis that cached when init the service, for the authorities info should be stored in auth center server.
            AuthorizeResponse authorizeResponse = new AuthorizeResponse(true, storedKey, false);
            authorizeResponse.set
            return
        } catch (RuntimeException e) {
            throw e;
        }
    }

    /**
     * 授权操作
     */
    @PostMapping(value = "/oauth2/authorize")
    public AuthorizeResponse authorize(@RequestBody AuthorizeRequest authorizeRequest) {
        String exchangeCode = authorizeRequest.getExchangeCode();
        if (exchangeCode == null) {
            throw new IllegalArgumentException("please provide exchange key.");
        }
        FirstAuthorizationRequest firstAuthorizationRequest = clientTokenProvider.validateToken(exchangeCode);
        if (firstAuthorizationRequest == null) {
            throw new AuthTimeOutException("authorize time out. please retry from original web side.");
        }


    }


    private String storeAuthRequest(@RequestParam Map<String, String> parameters) {
        FirstAuthorizationRequest firstAuthorizationRequest = buildFirstAuthRequest(parameters);
        return clientTokenProvider.storeToken(firstAuthorizationRequest);
    }

    private FirstAuthorizationRequest buildFirstAuthRequest(Map<String, String> parameters) {
        FirstAuthorizationRequest firstAuthorizationRequest = new FirstAuthorizationRequest();
        firstAuthorizationRequest.setClientId(parameters.get(OAuth2Utils.CLIENT_ID));
        firstAuthorizationRequest.setRedirectUri(parameters.get(OAuth2Utils.REDIRECT_URI));
        firstAuthorizationRequest.setResponseType(parameters.get(OAuth2Utils.RESPONSE_TYPE));
        firstAuthorizationRequest.setState(parameters.get(OAuth2Utils.STATE));
        return firstAuthorizationRequest;
    }


    private String generateCode(AuthorizationRequest authorizationRequest, Authentication authentication)
            throws AuthenticationException {
        try {
            OAuth2Request storedOAuth2Request = getOAuth2RequestFactory().createOAuth2Request(authorizationRequest);
            OAuth2Authentication combinedAuth = new OAuth2Authentication(storedOAuth2Request, authentication);
            return authorizationCodeServices.createAuthorizationCode(combinedAuth);
        } catch (OAuth2Exception e) {
            if (authorizationRequest.getState() != null) {
                e.addAdditionalInformation("state", authorizationRequest.getState());
            }
            throw e;
        }
    }

}
