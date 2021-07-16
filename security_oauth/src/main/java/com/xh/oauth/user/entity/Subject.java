package com.xh.oauth.user.entity;

import com.xh.oauth.utils.YesOrNoEnum;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Xiao Hong
 * @since 2020-12-09
 */
@Data
@Table
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Subject {

    @Id
    @GeneratedValue(generator = "idCreator",
            strategy = GenerationType.SEQUENCE)
    @GenericGenerator(
            name = "idCreator",
            strategy = "com.xh.oauth.utils.IdCreator"
    )
    private Long sid;

    @Column(unique = true)
    private String loginName;

    @Column
    private String password;

    @Column
    private String status;

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
