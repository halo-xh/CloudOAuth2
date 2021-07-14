package com.xh.oauth.domain;

import com.xh.oauth.utils.DeletedEnum;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author Xiao Hong
 * @since 2020-12-09
 */
@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
public class LoginToken {

    @Id
    @GeneratedValue(generator = "idCreator",
            strategy = GenerationType.SEQUENCE)
    @GenericGenerator(
            name = "idCreator",
            strategy = "com.xh.oauth.utils.IdCreator"
    )
    private Integer tid;

    @Column
    private String token;

    @Column
    private String user;

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
