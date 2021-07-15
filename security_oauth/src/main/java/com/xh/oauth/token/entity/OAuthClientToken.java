package com.xh.oauth.token.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

/**
 * @author xiaohong
 * @version 1.0
 * @date 2021/7/15 16:15
 * @description
 */
@Entity
@Data
@Table
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class OAuthClientToken {

    @Id
    private String tokenId;

    @Lob
    @Basic(fetch = FetchType.EAGER)
    @Column(name = "authentication", columnDefinition = "binary(1) DEFAULT NULL", nullable = true)
    private byte[] authentication;

    @Column(name = "authentication_id", columnDefinition = "varchar(256) NOT NULL ", nullable = false)
    private String authenticationId;

    @Column(name = "user_name", columnDefinition = "varchar(256)")
    private String userName;

    @Column(name = "client_id", columnDefinition = "varchar(256)")
    private String clientId;

}
