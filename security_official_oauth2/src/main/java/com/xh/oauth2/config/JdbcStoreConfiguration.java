package com.xh.oauth2.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.JdbcApprovalStore;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import javax.sql.DataSource;

/**
 * @author xiaohong
 * @version 1.0
 * @date 2021/7/19 13:58
 * @description
 */

@Configuration
public class JdbcStoreConfiguration {

    /**
     * oauth_client_details
     * @param dataSource
     * @return
     */
    @Bean
    public ClientDetailsService jdbcClientDetails(DataSource dataSource){
        return new JdbcClientDetailsService(dataSource);
    }

    /**
     * oauth_access_token && oauth_refresh_token
     */
    @Bean
    public TokenStore tokenStore(DataSource dataSource){
        return new JdbcTokenStore(dataSource);
    }

    /**
     * oauth_code
     * @param dataSource
     * @return
     */
    @Bean
    public JdbcAuthorizationCodeServices jdbcAuthorizationCodeServices(DataSource dataSource) {
        return new JdbcAuthorizationCodeServices(dataSource);
    }

    /**
     * oauth_approvals
     * @param dataSource
     * @return
     */
    @Bean
    public ApprovalStore approvalStore(DataSource dataSource){
        return new JdbcApprovalStore(dataSource);
    }
}
