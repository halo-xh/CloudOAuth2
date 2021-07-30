package com.xh.oauth.token.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

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
    private Long requestId;

    private String clientId;

    @Column(name = "token", columnDefinition = "varchar(2048)")
    private String token;

    private Date expireDate;

    @CreatedDate
    private Date createDate;

    @Column
    @LastModifiedDate
    private Date updateDate;

    @Version
    @Column
    private Integer version;

}
