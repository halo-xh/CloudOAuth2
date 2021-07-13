package com.xh.oauth.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * eg. [POST]/api/test1
 * <p>
 * Created by Xiao Hong on 2020-12-10
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResAuthMap implements Serializable {

    private Integer rid;// resource id
    private String method; // request method
    private String path; // request api
    private String authority; // role id
}
