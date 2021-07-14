package com.xh.oauth.config;

import com.xh.oauth.exception.AuthTokenExceptionHandler;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;

import javax.sql.DataSource;
import java.util.concurrent.TimeUnit;

/**
 * author  Xiao Hong
 * date  2021/7/13 23:41
 * description
 */
@Configuration
public class AuthServerConfiguration extends AuthorizationServerConfigurerAdapter {


    private final AuthenticationManager authenticationManager;

    private final UserDetailsService userDetailsService;

    private final PasswordEncoder passwordEncoder;

    private final TokenStore tokenStore;

    private final AuthorizationCodeServices authorizationCodeServices;

    private final AuthTokenExceptionHandler authTokenExceptionHandler;

    private final DataSource dataSource;

    private final AuthorizationServerTokenServices tokenService;

    public AuthServerConfiguration(AuthenticationManager authenticationManager,
                                   @Qualifier("myUserDetailServiceImpl") UserDetailsService userDetailsService,
                                   PasswordEncoder passwordEncoder,
                                   TokenStore tokenStore,
                                   AuthorizationCodeServices authorizationCodeServices,
                                   AuthTokenExceptionHandler authTokenExceptionHandler,
                                   DataSource dataSource,
                                   AuthorizationServerTokenServices tokenService) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.tokenStore = tokenStore;
        this.authorizationCodeServices = authorizationCodeServices;
        this.authTokenExceptionHandler = authTokenExceptionHandler;
        this.dataSource = dataSource;
        this.tokenService = tokenService;
    }

    /**
     * 配置客户端的信息获取
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.jdbc(dataSource);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security
                .passwordEncoder(passwordEncoder)                //设置密码编辑器
                .allowFormAuthenticationForClients()
                .tokenKeyAccess("permitAll()")                   //开启 /oauth/token_key 的访问权限控制
                .checkTokenAccess("permitAll()")                 //开启 /oauth/check_token 验证端口认证权限访问
        ;
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        // 配置授权服务器端点的属性
        endpoints.authenticationManager(authenticationManager)    //认证管理器
                .tokenStore(tokenStore)
                .tokenServices(tokenService)
                .authorizationCodeServices(authorizationCodeServices)
                .userDetailsService(userDetailsService)
                .exceptionTranslator(authTokenExceptionHandler);
    }
}
