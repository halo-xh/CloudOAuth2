package com.xh.oauth.domain;

import com.xh.oauth.utils.DeletedEnum;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

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
public class Resources {

    @Id
    @GeneratedValue(generator = "idCreator",
            strategy = GenerationType.SEQUENCE)
    @GenericGenerator(
            name = "idCreator",
            strategy = "com.xh.oauth.utils.IdCreator"
    )
    private Integer rid;

    @Column
    private String resName;

    @Column
    private String resType;

    @Column
    private String status;

    @Column
    private String path;

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
