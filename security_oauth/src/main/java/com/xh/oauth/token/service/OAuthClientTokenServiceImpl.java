package com.xh.oauth.token.service;

import com.xh.oauth.token.entity.OAuthClientToken;
import com.xh.oauth.token.repo.OAuthClientTokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author xiaohong
 * @version 1.0
 * @date 2021/7/15 16:52
 * @description
 */
@Slf4j
@Service
public class OAuthClientTokenServiceImpl implements OAuthClientTokenService {

    private final OAuthClientTokenRepository authClientTokenRepository;

    public OAuthClientTokenServiceImpl(OAuthClientTokenRepository authClientTokenRepository) {
        this.authClientTokenRepository = authClientTokenRepository;
    }



    @Override
    public void save(OAuthClientToken authClientToken) {
        authClientTokenRepository.save(authClientToken);
    }


    @Override
    public boolean existsUserToken(Long requestId, String authToken) {
        return authClientTokenRepository.findByRequestIdAndToken(requestId, authToken) != null;
    }
}
