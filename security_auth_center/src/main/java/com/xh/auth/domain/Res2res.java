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
 * mapping table: role -function, function - resource
 * </p>
 *
 * @author Xiao Hong
 * @since 2020-12-10
 */
@Data
@Table(name = "auth_res_res")
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Res2res {

    @Id
    @GeneratedValue(generator = "idCreator",
            strategy = GenerationType.SEQUENCE)
    @GenericGenerator(
            name = "idCreator",
            strategy = "com.xh.common.utils.IdCreator")
    private Long id;

    private Integer resId;

    private Integer parentId;

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
