package com.xh.oauth.clients.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.*;
/**
 * @author xiaohong
 * @version 1.0
 * @date 2021/7/14 9:58
 * @description
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyClientDetails implements ClientDetails {

    @Id
    private String clientId;

    private String clientSecret;

    private String scope;

    private String resourceIds;

    private String authorizedGrantTypes;

    private String registeredRedirectUris;

    private String autoApproveScopes;

    private String authorities;

    private Integer accessTokenValiditySeconds;

    private Integer refreshTokenValiditySeconds;

//    private Map<String, Object> additionalInformation = new LinkedHashMap<String, Object>();


    @Override
    public String getClientId() {
        return this.clientId;
    }

    @Override
    public Set<String> getResourceIds() {
        return split(this.resourceIds);
    }

    @Override
    public boolean isSecretRequired() {
        return this.clientSecret != null;
    }

    @Override
    public String getClientSecret() {
        return this.clientSecret;
    }

    @Override
    public boolean isScoped() {
        return this.scope != null && !this.scope.isEmpty();
    }

    @Override
    public Set<String> getScope() {
        return split(this.scope);
    }

    @Override
    public Set<String> getAuthorizedGrantTypes() {
        return split(this.authorizedGrantTypes);
    }

    @Override
    public Set<String> getRegisteredRedirectUri() {
        return split(this.registeredRedirectUris);
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        Set<String> split = split(this.authorities);
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>(split.size());
        for (String role : split) {
            grantedAuthorities.add(new SimpleGrantedAuthority(role));
        }
        return grantedAuthorities;
    }

    @Override
    public Integer getAccessTokenValiditySeconds() {
        return this.accessTokenValiditySeconds;
    }

    @Override
    public Integer getRefreshTokenValiditySeconds() {
        return this.refreshTokenValiditySeconds;
    }

    @Override
    public boolean isAutoApprove(String scope) {
        if (autoApproveScopes == null) {
            return false;
        }
        for (String auto : split(autoApproveScopes)) {
            if ("true".equals(auto) || scope.matches(auto)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Map<String, Object> getAdditionalInformation() {
        return null;
    }

    private Set<String> split(String s) {
        String[] split = s.split(",");
        return new HashSet<>(Arrays.asList(split));
    }
}
