package com.xh.oauth.security.filters;

import com.xh.common.domains.SubjectLogin;
import com.xh.oauth.security.authenticate.Oauth2Authentication;
import com.xh.oauth.token.provider.JwtUserTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * Filters incoming requests and installs a Spring Security principal if a header corresponding to a valid user is
 * found.
 */
@Component
public class JwtUserTokenFilter extends OncePerRequestFilter {

    public static final String BEARER_ = "Bearer ";

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final JwtUserTokenProvider jwtUserTokenProvider;

    public JwtUserTokenFilter(JwtUserTokenProvider jwtUserTokenProvider) {
        this.jwtUserTokenProvider = jwtUserTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Oauth2Authentication authentication = (Oauth2Authentication) securityContext.getAuthentication();
        if (authentication != null) {
            String jwt = resolveToken(request);
            if (StringUtils.hasText(jwt)) {
                SubjectLogin subjectLogin = jwtUserTokenProvider.validateToken(jwt);
                if (subjectLogin != null) {
                    UsernamePasswordAuthenticationToken userAuth = new UsernamePasswordAuthenticationToken(subjectLogin, "", null);
                    authentication.setUserAuthentication(userAuth);
                    securityContext.setAuthentication(authentication);
                }else {
                    logger.info("user jwt token validate failed.. may token time out.");
                }
            }
        }
        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_)) {
            return bearerToken;
        }
        return null;
    }


}
