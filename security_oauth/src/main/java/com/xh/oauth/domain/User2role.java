package com.xh.oauth.domain;

import com.xh.oauth.utils.YesOrNoEnum;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
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
@Entity
@Table
@EntityListeners(AuditingEntityListener.class)
public class User2role {

    @Id
    @GeneratedValue(generator = "idCreator",
            strategy = GenerationType.SEQUENCE)
    @GenericGenerator(
            name = "idCreator",
            strategy = "com.xh.oauth.utils.IdCreator"
    )
    private Integer id;

    private Integer userId;

    private String roleName;

    @Enumerated(EnumType.STRING)
    private YesOrNoEnum deleted = YesOrNoEnum.NO;

    @CreatedDate
    private Date createDate;

    @Column
    @LastModifiedDate
    private Date updateDate;

    @Version
    @Column
    private Integer version;


}
