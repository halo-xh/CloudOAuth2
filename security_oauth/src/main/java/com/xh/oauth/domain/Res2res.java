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
 * mapping table: role -function, function - resource
 * </p>
 *
 * @author Xiao Hong
 * @since 2020-12-10
 */
@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Res2res {

    @Id
    @GeneratedValue(generator = "idCreator",
            strategy = GenerationType.SEQUENCE)
    @GenericGenerator(
            name = "idCreator",
            strategy = "com.xh.oauth.utils.IdCreator"
    )
    private Integer id;

    @Column
    private Integer resId;

    @Column
    private Integer parentId;

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
