package com.xh.security_source.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import javax.annotation.Resource;
import javax.sql.DataSource;


/**
 * author  Xiao Hong
 * date  2020/12/1 21:40
 * description
 */

@Configuration
@EnableResourceServer //Convenient annotation for OAuth2 Resource Servers
public class ResourceConfig extends ResourceServerConfigurerAdapter {

    @Resource
    private DataSource dataSource;

    /**
     * token 持久化策略
     */
    @Bean
    public TokenStore jdbcTokenStore() {
        return new JdbcTokenStore(dataSource);
    }

    /**
     * 指定资源和对应存储方案
     *
     * @param resources
     * @throws Exception
     */
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId("test1").tokenStore(jdbcTokenStore());
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                //指定不同请求方式访问资源所需要的权限
                .antMatchers(HttpMethod.GET, "/**").access("#oauth2.hasScope('read')")
                .antMatchers(HttpMethod.POST, "/**").access("#oauth2.hasScope('write')")
                .antMatchers(HttpMethod.PATCH, "/**").access("#oauth2.hasScope('write')")
                .antMatchers(HttpMethod.PUT, "/**").access("#oauth2.hasScope('write')")
                .antMatchers(HttpMethod.DELETE, "/**").access("#oauth2.hasScope('write')")
                .and()
                .headers().addHeaderWriter(((httpServletRequest, httpServletResponse) -> {
            httpServletResponse.addHeader("Access-Control-Allow-Origin", "*");// 允许跨域
            if (httpServletRequest.getMethod().equals("OPTIONS")) {// 预检请求、 传达所有请求头信息
                httpServletResponse.setHeader("Access-Control-Allow-Methods", httpServletRequest.getHeader("Access-Control-Request-Methods"));
                httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));

            }
        }));

    }
}
