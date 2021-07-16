package com.xh.oauth.clients.entity;

import com.xh.oauth.utils.YesOrNoEnum;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;

import javax.persistence.*;
import java.util.*;

/**
 * @author xiaohong
 * @version 1.0
 * @date 2021/7/14 9:58
 * @description
 */
@Data
@Table
@Entity
@EntityListeners(AuditingEntityListener.class)
public class MyClientDetails implements ClientDetails {

    @Id
    @GeneratedValue(generator = "idCreator",
            strategy = GenerationType.SEQUENCE)
    @GenericGenerator(
            name = "idCreator",
            strategy = "com.xh.oauth.utils.IdCreator"
    )
    private Long id;

    @Column(name = "client_id", nullable = false, columnDefinition = "varchar(255) comment '客户端id'")
    private String clientId;

    @Column(name = "client_secret", nullable = false, columnDefinition = "varchar(255) comment '客户端密码'")
    private String clientSecret;

    @Column(name = "scope", nullable = false, columnDefinition = "varchar(255) comment 'scope'")
    private String scope;

    @Column(name = "resource_ids", nullable = false, columnDefinition = "varchar(1000) comment '资源ids'")
    private String resourceIds;

    @Column(name = "authorized_grant_types", nullable = false, columnDefinition = "varchar(1000) comment '授权类型'")
    private String authorizedGrantTypes;

    @Column(name = "registered_redirect_uris", nullable = false, columnDefinition = "varchar(1000) comment '重定向uri'")
    private String registeredRedirectUris;

    @Enumerated(EnumType.STRING)
    private YesOrNoEnum autoApproveScopes = YesOrNoEnum.NO;

    @Column(name = "authorities", nullable = false, columnDefinition = "varchar(1000) comment '权限'")
    private String authorities;

    @Column(name = "access_token_validity_seconds", nullable = false, columnDefinition = "int default 30 comment 'token过期时间'")
    private Integer accessTokenValiditySeconds;

    @Column(name = "refresh_token_validity_seconds", nullable = false, columnDefinition = "varchar(255)  default 3000 comment 'token刷新时间'")
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
        return autoApproveScopes ==YesOrNoEnum.YES;
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
