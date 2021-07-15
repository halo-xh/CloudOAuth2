package com.xh.auth.security;

import com.xh.auth.config.security.PermitAllConfigAtrribute;
import com.xh.auth.domain.Res2res;
import com.xh.auth.domain.Resources;
import com.xh.auth.domain.User2role;
import com.xh.auth.service.Res2resService;
import com.xh.auth.service.ResourcesService;
import com.xh.auth.service.User2roleService;
import com.xh.common.annotation.AnonymousAccess;
import com.xh.common.constants.MyConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Xiao Hong on 2020-12-10
 */
@Service
public class AuthorityService {

    public static final String ROLE_ADMIN = "role-admin";

    private final ResourcesService resourcesService;

    private final Res2resService res2resService;

    private final User2roleService user2roleService;

    @Autowired
    private ApplicationContext applicationContext;

    public AuthorityService(ResourcesService resourcesService, Res2resService res2resService, User2roleService user2roleService) {
        this.resourcesService = resourcesService;
        this.res2resService = res2resService;
        this.user2roleService = user2roleService;
    }

    public Map<RequestMatcher, Collection<ConfigAttribute>> initAuthorityMap(boolean permitAllUrl, List<String> whiteUrlList) {
        List<Resources> apiResList = resourcesService.findAllByResType(MyConstants.RES_TYPE_API);
        Map<RequestMatcher, Collection<ConfigAttribute>> map = new HashMap<>(apiResList.size());
        // white url
        if (whiteUrlList != null) {
            for (String url : whiteUrlList) {
                if (!StringUtils.isEmpty(url)) {
                    AntPathRequestMatcher urlMatcher = new AntPathRequestMatcher(url);
                    List<ConfigAttribute> configAttList = Collections.singletonList(new PermitAllConfigAtrribute());
                    map.put(urlMatcher, configAttList);
                }
            }
        }
        // add annotation configured white api
        Set<String> annotatedWhiteUrl = getAnnotatedWhiteUrl();
        if (!annotatedWhiteUrl.isEmpty()) {
            for (String url : annotatedWhiteUrl) {
                AntPathRequestMatcher urlMatcher = new AntPathRequestMatcher(url);
                List<ConfigAttribute> configAttList = Collections.singletonList(new PermitAllConfigAtrribute());
                map.put(urlMatcher, configAttList);
            }
        }
        // db config
        List<Resources> roleResList = resourcesService.findAllByResType(MyConstants.RES_TYPE_ROLE);
        Map<Integer, String> roleMap = roleResList.stream().collect(Collectors.toMap(Resources::getRid, Resources::getResName));
        List<Res2res> res2resMapping = res2resService.findAll();
        HashMap<Integer, String> res2RoleMap = new HashMap<>();
        for (Res2res res2res : res2resMapping) {// api res mapping to role id
            if (!res2RoleMap.containsKey(res2res.getResId())) {
                res2RoleMap.put(res2res.getResId(), roleMap.get(res2res.getParentId()));
            } else {
                res2RoleMap.put(res2res.getResId(), res2RoleMap.get(res2res.getResId()) + "," + roleMap.get(res2res.getParentId()));
            }
        }
        for (Resources resources : apiResList) {
            Integer apiResId = resources.getRid();
            String path = resources.getPath();
            RequestMatcher matcher = apiPathResolver(path);
            String roles = res2RoleMap.get(apiResId);
            if (roles != null) {
                if (roles.contains(",")) {
                    Collection<ConfigAttribute> atts = SecurityConfig.createListFromCommaDelimitedString(roles);
                    map.put(matcher, atts);
                } else {
                    map.put(matcher, Collections.singletonList(new SecurityConfig(roles.trim())));
                }
            } else {
                //默认 admin 可以访问所有
                map.put(matcher, Collections.singletonList(new SecurityConfig(ROLE_ADMIN)));
            }

        }
        //permit all
        if (permitAllUrl) {
            AntPathRequestMatcher matcher = new AntPathRequestMatcher("/**");
            List<ConfigAttribute> configAttList = new ArrayList<>();
            ConfigAttribute configAttr = new SecurityConfig(AuthenticatedVoter.IS_AUTHENTICATED_FULLY);
            configAttList.add(configAttr);
            map.put(matcher, configAttList);
        }

        return map;
    }

    public Collection<GrantedAuthority> getGrantedAuthorityByLoginName(Integer userId) {
        // get user role
        List<User2role> user2roleList = user2roleService.findByUserId(userId);//
        String roles = user2roleList.stream().map(User2role::getRoleName).map(String::valueOf).collect(Collectors.joining(","));
        // pack role. role-test,role-admin...
        return AuthorityUtils.commaSeparatedStringToAuthorityList(roles);
    }

    //[POST]/api/test
    private static RequestMatcher apiPathResolver(String apiPath) {
        RequestMatcher matcher = null;
        if (apiPath.startsWith("[") && apiPath.contains("]")) {
            matcher = getRequestMatcherFromApiPath(apiPath);
        } else {
            // 默认 get
            matcher = new AntPathRequestMatcher(apiPath, "GET");
        }
        return matcher;
    }

    private static AntPathRequestMatcher getRequestMatcherFromApiPath(String apiPath) {
        int endIndex = apiPath.indexOf("]");
        String api = apiPath.substring(endIndex + 1).trim();
        String method = apiPath.substring(1, endIndex);
        return new AntPathRequestMatcher(api, method);
    }

    private Set<String> getAnnotatedWhiteUrl() {
        // 搜寻匿名标记 url： @AnonymousAccess
        Map<RequestMappingInfo, HandlerMethod> handlerMethodMap = applicationContext.getBean(RequestMappingHandlerMapping.class).getHandlerMethods();
        Set<String> anonymousUrls = new HashSet<>();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> infoEntry : handlerMethodMap.entrySet()) {
            HandlerMethod handlerMethod = infoEntry.getValue();
            AnonymousAccess anonymousAccess = handlerMethod.getMethodAnnotation(AnonymousAccess.class);
            if (null != anonymousAccess) {
                anonymousUrls.addAll(infoEntry.getKey().getPatternsCondition().getPatterns());
            }
        }
        return anonymousUrls;
    }
}
