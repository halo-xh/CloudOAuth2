package com.xh.oauth.web;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * author  Xiao Hong
 * date  2020/12/6 17:48
 * description
 */
@Service
public interface AuthorityService {

    /**
     * 可以 利用缓存优化 缓存
     */
    Map<RequestMatcher, Collection<ConfigAttribute>> initAuthorityMap(boolean permitAllUrl, List<String> whiteUrlList);

    Collection<GrantedAuthority> getGrantedAuthorityByLoginName(String loginName);

}
