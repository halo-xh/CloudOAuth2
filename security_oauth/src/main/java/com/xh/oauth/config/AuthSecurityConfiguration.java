package com.xh.oauth.config;

import com.xh.oauth.clients.service.ClientDetailsService;
import com.xh.oauth.security.filters.ClientTokenFilter;
import com.xh.oauth.security.filters.JwtUserTokenFilter;
import com.xh.oauth.security.provider.ClientAuthenticationProvider;
import com.xh.oauth.security.provider.UserAuthenticationProvider;
import com.xh.oauth.token.ClientTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xiaohong
 * @version 1.0
 * @date 2021/7/14 11:18
 * @description
 */
@Configuration
@EnableWebSecurity
public class AuthSecurityConfiguration extends WebSecurityConfigurerAdapter {


    private final JwtUserTokenFilter jwtUserTokenFilter;

    private final ClientTokenFilter clientTokenFilter;

    private final PasswordEncoder passwordEncoder;

    private final ClientTokenProvider clientTokenProvider;

    private final ClientDetailsService clientDetailsService;

    public AuthSecurityConfiguration(JwtUserTokenFilter jwtUserTokenFilter,
                                     ClientTokenFilter clientTokenFilter,
                                     PasswordEncoder passwordEncoder,
                                     ClientTokenProvider clientTokenProvider,
                                     ClientDetailsService clientDetailsService) {
        this.jwtUserTokenFilter = jwtUserTokenFilter;
        this.clientTokenFilter = clientTokenFilter;
        this.passwordEncoder = passwordEncoder;
        this.clientTokenProvider = clientTokenProvider;
        this.clientDetailsService = clientDetailsService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests().antMatchers("/oauth/**", "/oauth2/**").permitAll()
                .and()
                .authorizeRequests().antMatchers(HttpMethod.TRACE, "**").denyAll()
                .and()
                .authorizeRequests().anyRequest().authenticated()
                .and()
                .csrf().disable()
                .addFilterBefore(jwtUserTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(clientTokenFilter, JwtUserTokenFilter.class)
                .headers().frameOptions().sameOrigin()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        List<AuthenticationProvider> providerList = new ArrayList<>();
        providerList.add(clientAuthenticationProvider());
        providerList.add(userAuthenticationProvider());
        ProviderManager authenticationManager = new ProviderManager(providerList);
        authenticationManager.setEraseCredentialsAfterAuthentication(true);
        return authenticationManager;
    }

    @Bean
    public ClientAuthenticationProvider clientAuthenticationProvider() {
        ClientAuthenticationProvider clientAuthenticationProvider = new ClientAuthenticationProvider();
        clientAuthenticationProvider.setClientDetailsService(clientDetailsService);
        clientAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        clientAuthenticationProvider.setClientTokenProvider(clientTokenProvider);
        return clientAuthenticationProvider;
    }

    @Bean
    public UserAuthenticationProvider userAuthenticationProvider() {
        return new UserAuthenticationProvider();
    }

}
