package com.xh.auth.config.security;

import com.xh.auth.jwt.JwtFilter;
import com.xh.auth.jwt.JwtAccessDeniedHandler;
import com.xh.auth.jwt.JwtAuthenticationEntryPoint;
import com.xh.auth.security.AuthorityService;
import com.xh.auth.security.DBAuthenticationProvider;
import com.xh.auth.security.MyUserDetailsService;
import com.xh.common.constants.MyConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * configuration for security
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final Logger log = LoggerFactory.getLogger(SecurityConfiguration.class);

    @Autowired
    @Qualifier("securityMetadataSource")
    private FilterInvocationSecurityMetadataSource URIFilterInvocationSecurityMetaSource;

    @Autowired
    private JwtFilter jwtFilter;

    @Resource
    @Qualifier("logoutSuccessHandler")
    private HttpStatusReturningLogoutSuccessHandler logoutSuccessHandler;

    @Resource
    private MyUserDetailsService userDetailsService;

    @Resource
    private AuthorityService authorityService;

    @Resource
    private JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Resource
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return authenticationManager();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests().antMatchers(HttpMethod.TRACE, "**").denyAll()
                .anyRequest().authenticated()
                .and()
                .csrf().disable()
                .addFilterBefore(filterSecurityInterceptor(), FilterSecurityInterceptor.class)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .headers().frameOptions().sameOrigin()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.logout().logoutSuccessHandler(logoutSuccessHandler);
        http.exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler);
    }

    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        List<AuthenticationProvider> providerList = new ArrayList<>();
        providerList.add(dbauthenticationprovider());
        ProviderManager authenticationManager = new ProviderManager(providerList);
        authenticationManager.setEraseCredentialsAfterAuthentication(true);
        return authenticationManager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DBAuthenticationProvider dbauthenticationprovider() {
        DBAuthenticationProvider dbAuthenticationProvider = new DBAuthenticationProvider();
        dbAuthenticationProvider.setUserDetailsService(userDetailsService);
        dbAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        dbAuthenticationProvider.setAuthorityService(authorityService);
        dbAuthenticationProvider.setForcePrincipalAsString(true);
        return dbAuthenticationProvider;
    }

    @Bean(name = "logoutSuccessHandler")
    public HttpStatusReturningLogoutSuccessHandler logoutSuccessHandler() {
        return new HttpStatusReturningLogoutSuccessHandler();
    }

    public AffirmativeBased accessDecisionManager() {
        List<AccessDecisionVoter<?>> constructArgs = new ArrayList<>();
        AuthenticatedVoter authenticatedVoter = new AuthenticatedVoter();
        RoleVoter roleVoter = new RoleVoter();
        roleVoter.setRolePrefix(MyConstants.VOTER_ROLE_PREFIX);
        PermitAllVoter permitAllVoter = new PermitAllVoter();
        constructArgs.add(authenticatedVoter);
        constructArgs.add(roleVoter);
        constructArgs.add(permitAllVoter);
        return new AffirmativeBased(constructArgs);
    }

    public FilterSecurityInterceptor filterSecurityInterceptor() {
        FilterSecurityInterceptor filterSecurityInterceptor = new FilterSecurityInterceptor();
        filterSecurityInterceptor.setAccessDecisionManager(accessDecisionManager());
        filterSecurityInterceptor.setSecurityMetadataSource(URIFilterInvocationSecurityMetaSource);
        return filterSecurityInterceptor;
    }

}
