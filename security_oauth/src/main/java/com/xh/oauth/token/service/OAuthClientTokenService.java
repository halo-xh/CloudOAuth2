package com.xh.oauth.token.service;

import com.xh.oauth.token.entity.OAuthClientToken;

/**
 * @author xiaohong
 * @version 1.0
 * @date 2021/7/15 16:32
 * @description
 */
public interface OAuthClientTokenService {


    void save(OAuthClientToken authClientToken);

    boolean existsUserToken(Long requestId, String authToken);
}
