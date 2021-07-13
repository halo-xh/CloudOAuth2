package com.xh.oauth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xh.domain.LoginToken;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Xiao Hong
 * @since 2020-12-09
 */
public interface LoginTokenService extends IService<LoginToken> {

    boolean existsUserToken(String username, String authToken);

    boolean delIfExistsUserSession(String username);

    int saveToken(LoginToken token);
}
