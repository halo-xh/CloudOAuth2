package com.xh.common.domains;

import com.xh.common.utils.DeletedEnum;
import lombok.Data;

import java.util.Date;

@Data
public class SubjectLogin {

    private Long sid;

    private String loginName;

    private String password;

    private String status;

    private Date createDate;

    private Date updateDate;

    private DeletedEnum deleted = DeletedEnum.NO;

    private Integer version;

}

