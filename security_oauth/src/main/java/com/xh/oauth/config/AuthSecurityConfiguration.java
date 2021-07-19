package com.xh.oauth.config;

import com.xh.oauth.security.jwt.JWTFilter;
import com.xh.oauth.user.service.ExtUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author xiaohong
 * @version 1.0
 * @date 2021/7/14 11:18
 * @description
 */
@Configuration
@EnableWebSecurity
public class AuthSecurityConfiguration extends WebSecurityConfigurerAdapter {


    private final JWTFilter jwtFilter;

    public AuthSecurityConfiguration(JWTFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests().antMatchers("/oauth/**","/oauth2/**").permitAll()
        .and()
            .authorizeRequests().antMatchers(HttpMethod.TRACE, "**").denyAll()
        .and()
            .authorizeRequests().anyRequest().authenticated()
        .and()
            .csrf().disable()
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
            .headers().frameOptions().sameOrigin()
        .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
