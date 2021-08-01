package com.xh.thirdclient.domain;

import com.xh.common.enums.YesOrNoEnum;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

/**
 * author  Xiao Hong
 * date  2021/7/30 23:45
 * description
 */
@Data
@Table(name = "tc_author")
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Author {

    @Id
    @GeneratedValue(generator = "idCreator",
            strategy = GenerationType.SEQUENCE)
    @GenericGenerator(
            name = "idCreator",
            strategy = "com.xh.thirdclient.utils.IdCreator"
    )
    private Long aid;

    private String name;

    private Integer age;

    @Enumerated(EnumType.STRING)
    private YesOrNoEnum deleted = YesOrNoEnum.NO;

    @CreatedDate
    private Date createDate;

    @LastModifiedDate
    private Date updateDate;

    @Version
    private Integer version;


}
