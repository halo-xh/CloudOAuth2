package com.xh.oauth.utils;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * author  Xiao Hong
 * date  2021/7/17 16:46
 * description
 */
@Component
public class RedisUtils {

    private final RedisTemplate<String, String> redisTemplate;

    public RedisUtils(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public String getVal(String key) {
        return redisTemplate.opsForValue().get(key);
    }

}

