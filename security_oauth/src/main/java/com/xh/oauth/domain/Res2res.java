package com.xh.oauth.domain;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
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
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "Res2res对象", description = "mapping table: role -function, function - resource")
public class Res2res implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "table id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "resource id: function, resource")
    private Integer resid;

    @ApiModelProperty(value = "resource id : function , role")
    private Integer parentid;

    @TableField(fill = FieldFill.INSERT)
    private Date createdt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updatedt;

    @Version
    private Integer version;


}
