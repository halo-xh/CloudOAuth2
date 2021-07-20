package com.xh.oauth.security.jwt;

import com.xh.common.domains.SubjectLogin;
import com.xh.common.feign.AuthFeign;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author xiaohong
 * @version 1.0
 * @date 2021/7/20 14:06
 * @description
 */
@Service
public class JwtTokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);


    @Autowired
    private AuthFeign authFeign;


    public SubjectLogin validateToken(String jwt) {
        logger.info("validate jwt token...");
        return authFeign.validateToken(jwt);
    }

}
