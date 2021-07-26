package com.xh.oauth.token;

public interface TokenStore<T> {

    String store(T token);

    T get(String key);
}
