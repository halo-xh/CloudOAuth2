package com.xh.auth.service;


import com.xh.auth.domain.LoginToken;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Xiao Hong
 * @since 2020-12-09
 */
public interface LoginTokenService {

    boolean existsUserToken(String username, String authToken);

    boolean delIfExistsUserSession(String username);

    int saveToken(LoginToken token);
}
