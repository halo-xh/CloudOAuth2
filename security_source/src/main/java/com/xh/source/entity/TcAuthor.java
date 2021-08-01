package com.xh.source.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author xh
 * @since 2021-07-31
 */
@Data
@Accessors(chain = true)
public class TcAuthor {

    @TableId(type = IdType.ASSIGN_ID)
    private Long aid;

    private Integer age;

    private LocalDateTime createDate;

    private String deleted;

    private String name;

    private LocalDateTime updateDate;

    private Integer version;


}
