package com.xh.auth.config.security;

import com.xh.auth.security.AuthorityService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by Xiao Hong on 2020-12-10
 */
@Component("securityMetadataSource")
public class URIFilterInvocationSecurityMetaSource implements FilterInvocationSecurityMetadataSource {

    public URIFilterInvocationSecurityMetaSource(AuthorityService authorityService) {
        this.requestMap = authorityService.initAuthorityMap(permitAllUrl, whiteUrlList);
    }

    private final List<String> whiteUrlList = new ArrayList<String>() {{
        add("/");
        add("/**/*.css");
        add("/**/*.js");
        add("/**/*.json");
        add("/images/**");
        add("/fonts/**");
        add("/**.js.map");
        add("/**.png");
        add("/**.woff");
        add("/**.ttf");
        add("/i18n/**");
        add("/index.html");
        add("/api/logout");
        add("/favicon.ico");
        add("/api/subject-login/authenticate"); // login path
        add("/swagger-resources/**"); //swagger
        add("/v2/api-docs");
        add("/swagger-ui.html");
        // todo.
//        add("/api/subject/findByName");
    }};

    //    @Value("${app.config.security.permit-all}") todo.
    private boolean permitAllUrl = false;

    // <request api matcher,List<role_string>>
    private Map<RequestMatcher, Collection<ConfigAttribute>> requestMap;

    @Override
    public Collection<ConfigAttribute> getAttributes(Object invocation) throws IllegalArgumentException {
        final HttpServletRequest request = ((FilterInvocation) invocation).getRequest();
        for (Map.Entry<RequestMatcher, Collection<ConfigAttribute>> entry : requestMap.entrySet()) {
            if (entry.getKey().matches(request)) {
                return entry.getValue();
            }
        }
        return null;
    }


    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        Set<ConfigAttribute> allAttributes = new HashSet<>();
        for (Map.Entry<RequestMatcher, Collection<ConfigAttribute>> entry : requestMap.entrySet()) {
            allAttributes.addAll(entry.getValue());
        }
        return allAttributes;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }
}
