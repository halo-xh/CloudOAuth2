package com.xh.oauth.config;

import com.xh.oauth.exception.AuthClientExceptionHandler;
import com.xh.oauth.exception.AuthTokenExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.jwt.crypto.sign.MacSigner;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.InMemoryAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

/**
 * author  Xiao Hong
 * date  2021/7/13 23:51
 * description
 */
@Configuration
public class AuthenticationBeans {

    @Bean
    public AuthClientExceptionHandler authClientExceptionHandler() {
        return new AuthClientExceptionHandler();
    }

    @Bean
    public AuthTokenExceptionHandler authTokenExceptionHandler() {
        return new AuthTokenExceptionHandler();
    }

    /**
     * 密码解析器
     **/
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * token 存储器
     **/
    @Bean
    public TokenStore tokenStore(RedisConnectionFactory redisConnectionFactory) {
        return new RedisTokenStore(redisConnectionFactory);
    }

    /**
     * token 存储器 JWT
     **/
//    @Bean
    public TokenStore tokenStore(JwtAccessTokenConverter jwtAccessTokenConverter) {
        return new JwtTokenStore(jwtAccessTokenConverter);
    }

    @Bean
    @Primary
    public AuthorizationServerTokenServices defaultTokenServices(TokenStore tokenStore,
                                                                 JwtAccessTokenConverter jwtAccessTokenConverter,
                                                                 ClientDetailsService clientDetailsService,
                                                                 AuthenticationManager authenticationManager) {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setSupportRefreshToken(true);
        defaultTokenServices.setTokenStore(tokenStore);
        defaultTokenServices.setTokenEnhancer(jwtAccessTokenConverter);
        defaultTokenServices.setAccessTokenValiditySeconds(3600);
        defaultTokenServices.setRefreshTokenValiditySeconds(7200);
        defaultTokenServices.setClientDetailsService(clientDetailsService);
        defaultTokenServices.setAuthenticationManager(authenticationManager);

        return defaultTokenServices;
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        // 签名密钥
        jwtAccessTokenConverter.setSigningKey("sign_key");
        // 验证密钥
        jwtAccessTokenConverter.setVerifier(new MacSigner("sign_key"));
        return jwtAccessTokenConverter;
    }

    /**
     * code 生成服务
     **/
    @Bean
    public AuthorizationCodeServices authorizationCodeServices() {
        return new InMemoryAuthorizationCodeServices();
    }
}
