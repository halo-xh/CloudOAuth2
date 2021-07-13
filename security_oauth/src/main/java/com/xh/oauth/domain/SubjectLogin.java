package com.xh.oauth.domain;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
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
@Builder
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "SubjectLogin对象", description = "store login user details")
public class SubjectLogin implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "sid", type = IdType.AUTO)
    private Integer sid;

    private String loginname;

    private String password;

    private String status;

    @TableField(fill = FieldFill.INSERT)
    private Date createdt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updatedt;

    @TableLogic
    private String deleted;

    @Version
    private Integer version;


}
