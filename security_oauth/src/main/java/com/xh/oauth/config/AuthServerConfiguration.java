package com.xh.oauth.config;

import com.xh.oauth.exception.AuthTokenExceptionHandler;
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
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;


/**
 * author  Xiao Hong
 * date  2021/7/13 23:41
 * description 授权服务配置类 {@link EnableAuthorizationServer}
 */
//@Configuration
//@EnableAuthorizationServer
//@Deprecated
public class AuthServerConfiguration extends AuthorizationServerConfigurerAdapter {


    private final AuthenticationManager authenticationManager; //todo.

    private final UserDetailsService userDetailsService;

    private final PasswordEncoder passwordEncoder;

    private final ApprovalStore approvalStore;

    private final TokenStore tokenStore;// todo. db - redis

    private final AuthorizationCodeServices authorizationCodeServices;

    private final AuthTokenExceptionHandler authTokenExceptionHandler;

    private final ClientDetailsServiceBuilder<JdbcClientDetailsServiceBuilder> jdbcClientDetailsServiceBuilder;


    public AuthServerConfiguration(AuthenticationManager authenticationManager,
                                   UserDetailsService userDetailsService,
                                   PasswordEncoder passwordEncoder,
                                   ApprovalStore approvalStore,
                                   TokenStore tokenStore,
                                   AuthorizationCodeServices authorizationCodeServices,
                                   AuthTokenExceptionHandler authTokenExceptionHandler,
                                   ClientDetailsServiceBuilder<JdbcClientDetailsServiceBuilder> jdbcClientDetailsServiceBuilder
                                   ) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.approvalStore = approvalStore;
        this.tokenStore = tokenStore;
        this.authorizationCodeServices = authorizationCodeServices;
        this.authTokenExceptionHandler = authTokenExceptionHandler;
        this.jdbcClientDetailsServiceBuilder = jdbcClientDetailsServiceBuilder;
    }

    /**
     * 配置客户端的信息获取
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients
                .setBuilder(jdbcClientDetailsServiceBuilder);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security
                //设置密码编辑器
                .passwordEncoder(passwordEncoder)
//                .allowFormAuthenticationForClients()
                //开启 /oauth/token_key 的访问权限控制
                .tokenKeyAccess("permitAll()")
                //开启 /oauth/check_token 验证端口认证权限访问
                .checkTokenAccess("permitAll()")
        ;
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        // 配置授权服务器端点的属性
        endpoints
                //认证管理器
                .authenticationManager(authenticationManager)
                .tokenStore(tokenStore)
                .approvalStore(approvalStore)
                .authorizationCodeServices(authorizationCodeServices)
                .userDetailsService(userDetailsService)
//                .pathMapping("/oauth/authorize", "/oauth2/authorize")
                .exceptionTranslator(authTokenExceptionHandler);
    }
}
