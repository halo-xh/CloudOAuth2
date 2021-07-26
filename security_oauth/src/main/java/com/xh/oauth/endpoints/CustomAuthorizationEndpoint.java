package com.xh.oauth.endpoints;

import com.xh.oauth.clients.entity.ClientDetails;
import com.xh.oauth.clients.service.ClientDetailsService;
import com.xh.oauth.endpoints.request.AuthorizeRequest;
import com.xh.oauth.endpoints.request.AuthorizationRequest;
import com.xh.oauth.endpoints.request.OAuth2Request;
import com.xh.oauth.endpoints.request.OAuth2RequestFactory;
import com.xh.oauth.endpoints.response.AuthorizeResponse;
import com.xh.oauth.exception.InvalidClientException;
import com.xh.oauth.exception.OAuth2Exception;
import com.xh.oauth.exception.UnauthorizedClientException;
import com.xh.oauth.exception.UnsupportedResponseTypeException;
import com.xh.oauth.security.authenticate.AuthorizationCodeServices;
import com.xh.oauth.security.authenticate.ClientAuthentication;
import com.xh.oauth.security.authenticate.Oauth2Authentication;
import com.xh.oauth.security.authenticate.Oauth2Request;
import com.xh.oauth.token.ClientTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * author  Xiao Hong
 * date  2021/7/17 12:44
 * description 授权码模式控制器 参考:{@link org.springframework.security.oauth2.provider.endpoint .AuthorizationEndpoint}
 */
@RequestMapping("/oauth2")
@RestController
public class CustomAuthorizationEndpoint {

    private static final Logger logger = LoggerFactory.getLogger(CustomAuthorizationEndpoint.class);

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
    public ResponseEntity<AuthorizeResponse> authorize(@RequestParam Map<String, String> parameters) {
        // 构建参数
        AuthorizationRequest authorizationRequest = oAuth2RequestFactory.createAuthorizationRequest(parameters);
        // 尝试获取 Authentication 判断是不是要进行验证
        Oauth2Authentication authentication = null;
        try {
            Authentication au = SecurityContextHolder.getContext().getAuthentication();
            if (au instanceof Oauth2Authentication) {
                authentication = (Oauth2Authentication) au;
            } else {
                throw new UnauthorizedClientException("unauthorized");
            }
        } catch (Exception e) {
            logger.error("cannot get authentication. user haven't login...");
            throw new UnauthorizedClientException("unauthorized");
        }
        ClientAuthentication clientAuthentication = authentication.getClientAuthentication();
        if (clientAuthentication == null) {
            throw new UnauthorizedClientException("unauthorized");
        }
        // 保存此次请求信息
        String storedKey = storeAuthRequest(parameters, authentication);
        Oauth2Request principal = (Oauth2Request) authentication.getPrincipal();
        // set client passed header
        Long requestId = principal.getRequestId();
        String cachedToken = clientTokenProvider.getCached(requestId);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer " + cachedToken);
        /* ==== user not login==== */
        Authentication userAuthentication = authentication.getUserAuthentication();
        if (userAuthentication == null) {
            AuthorizeResponse authorizeResponse = new AuthorizeResponse(false, storedKey, false);
            return new ResponseEntity<>(authorizeResponse, httpHeaders, HttpStatus.OK);
        }
        /* ====user already authenticated==== */
        Collection<? extends GrantedAuthority> authorities = userAuthentication.getAuthorities();
        AuthorizeResponse authorizeResponse = new AuthorizeResponse(true, storedKey, false);
        authorizeResponse.setAuthorities(getAuthorities(authorities));
        return new ResponseEntity<>(authorizeResponse, httpHeaders, HttpStatus.OK);
    }

    private String storeAuthRequest(Map<String, String> parameters, Oauth2Authentication authentication) {
        return null;
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
//        //todo. here 07-17
        return null;
    }


    // after granted
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

    private List<String> getAuthorities(Collection<? extends GrantedAuthority> authorities) {
        ArrayList<String> arrayList = new ArrayList<>(authorities.size());
        for (GrantedAuthority authority : authorities) {
            String s = authority.getAuthority();
            arrayList.add(s);
        }
        return arrayList;
    }

}
