package com.xh.auth.service.impl;

import com.xh.auth.domain.LoginToken;
import com.xh.auth.repo.LoginTokenRepository;
import com.xh.auth.service.LoginTokenService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Xiao Hong
 * @since 2020-12-09
 */
@Service
public class LoginTokenServiceImpl implements LoginTokenService {

    private final LoginTokenRepository loginTokenRepository;

    public LoginTokenServiceImpl(LoginTokenRepository loginTokenRepository) {
        this.loginTokenRepository = loginTokenRepository;
    }

    @Override
    public boolean existsUserToken(String username, String authToken) {
        LoginToken loginToken = loginTokenRepository.findByUserNameAndToken(username, authToken);
        return loginToken != null;
    }

    @Override
    public boolean delIfExistsUserSession(String username) {
        LoginToken loginToken = loginTokenRepository.findByUserName(username);
        if (loginToken != null) {
            loginTokenRepository.delete(loginToken);
        }
        return true;
    }

    @Override
    public int saveToken(LoginToken token) {
        LoginToken save = loginTokenRepository.save(token);
        return 1;
    }

}
