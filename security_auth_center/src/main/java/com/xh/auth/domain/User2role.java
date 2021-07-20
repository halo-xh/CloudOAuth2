package com.xh.auth.domain;

import com.xh.common.utils.DeletedEnum;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * user mapping role.
 * </p>
 *
 * @author Xiao Hong
 * @since 2020-12-10
 */
@Data
@Table(name = "auth_user_role")
@Entity
@EntityListeners(AuditingEntityListener.class)
public class User2role {

    @Id
    @GeneratedValue(generator = "idCreator",
            strategy = GenerationType.SEQUENCE)
    @GenericGenerator(
            name = "idCreator",
            strategy = "com.xh.common.utils.IdCreator")
    private Long id;

    private Long userId;

    private String roleName;

    @CreatedDate
    private Date createDate;

    @Column
    @LastModifiedDate
    private Date updateDate;

    @Enumerated(EnumType.STRING)
    private DeletedEnum deleted = DeletedEnum.NO;

    @Version
    @Column
    private Integer version;

}
