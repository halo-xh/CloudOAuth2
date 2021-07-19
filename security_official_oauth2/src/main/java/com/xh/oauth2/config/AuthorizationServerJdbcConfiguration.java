package com.xh.oauth2.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.TokenStore;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerJdbcConfiguration extends AuthorizationServerConfigurerAdapter {

    @Qualifier("customUserDetailService")
    @Autowired
    private UserDetailsService userDetailsService;
    /**
     * 依托jdbc实现的客户端详情（也可以是其他存储介质）
     */
    @Autowired
    private ClientDetailsService jdbcClientDetails;

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private JdbcAuthorizationCodeServices jdbcAuthorizationCodeServices;

    @Autowired
    private ApprovalStore approvalStore;

    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * 配置客户端信息
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
//加载自定义的客户端详情
// 如果只有jdbc实现，也可以使用内置方法注入 datasource 即可: clients.jdbc(datasource);
        clients.withClientDetails(jdbcClientDetails);
//        clients.jdbc(dataSource);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        // token 存储
        endpoints.tokenStore(tokenStore)
                // code存储
                .authorizationCodeServices(jdbcAuthorizationCodeServices)
                // 用户加载
                .userDetailsService(userDetailsService)
                // 授权记录
                .approvalStore(approvalStore)
                // 密码模式需要配置该项
                .authenticationManager(authenticationManager)
        ;
    }
}
