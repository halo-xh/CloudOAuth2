package com.xh.auth.domain;

import com.xh.common.utils.DeletedEnum;
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
@Table(name = "auth_subject_login")
@Entity
@EntityListeners(AuditingEntityListener.class)
public class SubjectLogin {

    @Id
    @GeneratedValue(generator = "idCreator",
            strategy = GenerationType.SEQUENCE)
    @GenericGenerator(
            name = "idCreator",
            strategy = "com.xh.common.utils.IdCreator")
    private Long sid;

    @Column(unique = true, nullable = false)
    private String loginName;

    private String password;

    private String status;

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
