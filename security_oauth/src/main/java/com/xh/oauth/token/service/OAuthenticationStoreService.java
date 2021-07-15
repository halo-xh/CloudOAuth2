package com.xh.oauth.token.service;

import com.xh.oauth.token.entity.OAuthCode;

public interface OAuthenticationStoreService {

    OAuthCode save(OAuthCode authStore);

    OAuthCode remove(String code);
}
