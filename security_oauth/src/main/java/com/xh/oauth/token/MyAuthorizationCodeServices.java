package com.xh.oauth.token;

import com.xh.oauth.token.entity.OAuthStore;
import com.xh.oauth.token.service.OAuthenticationStoreService;
import com.xh.oauth.utils.DeletedEnum;
import com.xh.oauth.utils.IdCreator;
import com.xh.oauth.utils.SnowflakeIdWorker;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.RandomValueAuthorizationCodeServices;
import org.springframework.util.SerializationUtils;

/**
 * author  Xiao Hong
 * date  2021/7/14 23:25
 * description 可以选择继承 {@link RandomValueAuthorizationCodeServices}
 */
public class MyAuthorizationCodeServices implements AuthorizationCodeServices {

    private final OAuthenticationStoreService authenticationStoreService;

    private final SnowflakeIdWorker idWorker;

    public MyAuthorizationCodeServices(OAuthenticationStoreService oAuthenticationStoreService, SnowflakeIdWorker idWorker) {
        this.authenticationStoreService = oAuthenticationStoreService;
        this.idWorker = idWorker;
    }

    /**
     * @param authentication OAuth2Authentication
     * @return code
     */
    @Override
    public String createAuthorizationCode(OAuth2Authentication authentication) {
        String code = String.valueOf(idWorker.nextId());
        byte[] serialize = SerializationUtils.serialize(authentication);
        OAuthStore authStore = new OAuthStore(code, serialize, DeletedEnum.NO);
        OAuthStore save = authenticationStoreService.save(authStore);
        return save.getCode();
    }

    @Override
    public OAuth2Authentication consumeAuthorizationCode(String code) throws InvalidGrantException {
        OAuthStore remove = authenticationStoreService.remove(code);
        OAuth2Authentication auth = (OAuth2Authentication) SerializationUtils.deserialize(remove.getAuthentication());
        if (auth == null) {
            throw new InvalidGrantException("Invalid authorization code: " + code);
        }
        return auth;
    }
}
