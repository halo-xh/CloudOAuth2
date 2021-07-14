package com.xh.oauth.config;

import com.xh.oauth.clients.MyJdbcClientDetailsServiceBuilder;
import com.xh.oauth.exception.AuthTokenExceptionHandler;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.builders.ClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.builders.JdbcClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;

import javax.sql.DataSource;

/**
 * author  Xiao Hong
 * date  2021/7/13 23:41
 * description 授权服务配置类 {@link EnableAuthorizationServer}
 */
@Configuration
@EnableAuthorizationServer
public class AuthServerConfiguration extends AuthorizationServerConfigurerAdapter {


    private final AuthenticationManager authenticationManager; //todo.

    private final UserDetailsService userDetailsService;

    private final PasswordEncoder passwordEncoder;

    private final TokenStore tokenStore;// todo. db - redis

    private final AuthorizationCodeServices authorizationCodeServices;

    private final AuthTokenExceptionHandler authTokenExceptionHandler;

    private final AuthorizationServerTokenServices tokenService;

    private final ClientDetailsServiceBuilder<MyJdbcClientDetailsServiceBuilder> jdbcClientDetailsServiceBuilder;

    public AuthServerConfiguration(AuthenticationManager authenticationManager,
                                   UserDetailsService userDetailsService,
                                   PasswordEncoder passwordEncoder,
                                   TokenStore tokenStore,
                                   AuthorizationCodeServices authorizationCodeServices,
                                   AuthTokenExceptionHandler authTokenExceptionHandler,
                                   AuthorizationServerTokenServices tokenService,
                                   ClientDetailsServiceBuilder<MyJdbcClientDetailsServiceBuilder> jdbcClientDetailsServiceBuilder) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.tokenStore = tokenStore;
        this.authorizationCodeServices = authorizationCodeServices;
        this.authTokenExceptionHandler = authTokenExceptionHandler;
        this.tokenService = tokenService;
        this.jdbcClientDetailsServiceBuilder = jdbcClientDetailsServiceBuilder;
    }

    /**
     * 配置客户端的信息获取
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.configure(jdbcClientDetailsServiceBuilder);
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
