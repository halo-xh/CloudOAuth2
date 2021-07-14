package com.xh.oauth.token.service;

import com.xh.oauth.token.entity.OAuthStore;

public interface OAuthenticationStoreService {

    OAuthStore save(OAuthStore authStore);

    OAuthStore remove(String code);
}
