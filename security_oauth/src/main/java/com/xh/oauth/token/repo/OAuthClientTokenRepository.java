package com.xh.oauth.token.repo;

import com.xh.oauth.token.entity.OAuthClientToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author xiaohong
 * @version 1.0
 * @date 2021/7/15 16:31
 * @description
 */
public interface OAuthClientTokenRepository extends JpaRepository<OAuthClientToken, String>, JpaSpecificationExecutor<OAuthClientToken> {


    OAuthClientToken findByRequestIdAndToken(Long reqId,String token);

}
