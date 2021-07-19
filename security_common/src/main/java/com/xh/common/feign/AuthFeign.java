package com.xh.common.feign;

import com.xh.common.domains.SubjectLogin;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author xiaohong
 * @version 1.0
 * @date 2021/7/19 14:19
 * @description
 */
@FeignClient(name = "auth-center", contextId = "auth",url="127.0.0.1:9090", path = "/auth_center")
public interface AuthFeign {

    @PostMapping("/api/subject/findByName")
    SubjectLogin findByName(@RequestBody String userName);

}
