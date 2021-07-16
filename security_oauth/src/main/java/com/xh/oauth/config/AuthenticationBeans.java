package com.xh.oauth.config;

import com.xh.oauth.clients.MyClientDetailsService;
import com.xh.oauth.clients.MyJdbcClientDetailsServiceBuilder;
import com.xh.oauth.clients.service.DClientDetailsService;
import com.xh.oauth.exception.AuthClientExceptionHandler;
import com.xh.oauth.exception.AuthTokenExceptionHandler;
import com.xh.oauth.token.MyAuthorizationCodeServices;
import com.xh.oauth.token.service.OAuthenticationStoreService;
import com.xh.oauth.utils.SnowflakeIdWorker;
import com.xh.oauth.web.AuthorityService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.jwt.crypto.sign.MacSigner;
import org.springframework.security.oauth2.config.annotation.builders.ClientDetailsServiceBuilder;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.TokenApprovalStore;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import javax.sql.DataSource;

/**
 * author  Xiao Hong
 * date  2021/7/13 23:51
 * description
 */
@Configuration
public class AuthenticationBeans {

    // ========================================================
    // ==================CLIENTS CONFIGURATION=================
    // ========================================================

    /**
     * 密码解析器
     **/
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * A service that provides the details about an OAuth2 client.
     *
     * @return service
     */
    @Bean(name = "myClientDetailsService")
    public MyClientDetailsService clientDetailsService(DClientDetailsService detailsService) {
        return new MyClientDetailsService(detailsService);
    }

    /**
     * Builder for OAuth2 client details service.
     */
    @Bean(name = "jdbcClientDetailsServiceBuilder")
    public ClientDetailsServiceBuilder<MyJdbcClientDetailsServiceBuilder>
    clientDetailsServiceBuilder(DataSource dataSource,
                                MyClientDetailsService clientDetailsService) {
        MyJdbcClientDetailsServiceBuilder jdbcClientDetailsServiceBuilder = new MyJdbcClientDetailsServiceBuilder();
        jdbcClientDetailsServiceBuilder.setDataSource(dataSource);
        jdbcClientDetailsServiceBuilder.setMyClientDetailsService(clientDetailsService);
        return jdbcClientDetailsServiceBuilder;
    }


    /**
     * Services for issuing and storing authorization codes.
     **/
    @Bean
    public AuthorizationCodeServices authorizationCodeServices(SnowflakeIdWorker idWorker, OAuthenticationStoreService authenticationStoreService) {
        return new MyAuthorizationCodeServices(authenticationStoreService, idWorker);
    }


    @Bean
    public AuthClientExceptionHandler authClientExceptionHandler() {
        return new AuthClientExceptionHandler();
    }

    @Bean
    public AuthTokenExceptionHandler authTokenExceptionHandler() {
        return new AuthTokenExceptionHandler();
    }

    /**
     * token 存储器
     **/
    @Bean
    public TokenStore tokenStore(RedisConnectionFactory redisConnectionFactory) {
        return new RedisTokenStore(redisConnectionFactory);
    }

    @Bean
    public ApprovalStore approvalStore(TokenStore tokenStore){
        TokenApprovalStore tokenApprovalStore = new TokenApprovalStore();
        tokenApprovalStore.setTokenStore(tokenStore);
        return tokenApprovalStore;
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
     * token 存储器 JWT
     **/
//    @Bean
    public TokenStore tokenStore(JwtAccessTokenConverter jwtAccessTokenConverter) {
        return new JwtTokenStore(jwtAccessTokenConverter);
    }

    @Bean
    @Primary //todo.
    public AuthorizationServerTokenServices defaultTokenServices(TokenStore tokenStore,
                                                                 JwtAccessTokenConverter jwtAccessTokenConverter,
                                                                 @Qualifier("myClientDetailsService") ClientDetailsService clientDetailsService,
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


    // ========================================================
    // ======================USER AUTH CONFIG==================
    // ========================================================
//    @Bean
//    public DBAuthenticationProvider dbauthenticationprovider(UserDetailsService userDetailsService, AuthorityService authorityService) {
//        DBAuthenticationProvider dbAuthenticationProvider = new DBAuthenticationProvider();
//        dbAuthenticationProvider.setUserDetailsService(userDetailsService);
//        dbAuthenticationProvider.setPasswordEncoder(passwordEncoder());
//        dbAuthenticationProvider.setAuthorityService(authorityService);// todo.
//        dbAuthenticationProvider.setForcePrincipalAsString(true);
//        return dbAuthenticationProvider;
//    }

}
