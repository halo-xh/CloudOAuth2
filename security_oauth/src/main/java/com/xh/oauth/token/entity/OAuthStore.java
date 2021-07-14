package com.xh.oauth.token.entity;

import com.xh.oauth.utils.DeletedEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.util.Date;

/**
 * author  Xiao Hong
 * date  2021/7/14 23:33
 * description
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OAuthStore {

    public OAuthStore(String code, byte[] authentication, DeletedEnum deleted) {
        this.code = code;
        this.authentication = authentication;
        this.deleted = deleted;
    }

    @Id
    @GeneratedValue(generator = "idCreator",
            strategy = GenerationType.SEQUENCE)
    @GenericGenerator(
            name = "idCreator",
            strategy = "com.xh.oauth.utils.IdCreator"
    )
    private String code;

    @Lob
    @Basic(fetch = FetchType.EAGER)
    @Column(name = " authentication", columnDefinition = "authentication", nullable = false)
    private byte[] authentication;

    @Enumerated(EnumType.STRING)
    private DeletedEnum deleted = DeletedEnum.NO;

    @CreatedDate
    private Date createDate;

    @Column
    @LastModifiedDate
    private Date updateDate;

    @Version
    @Column
    private Integer version;

}
