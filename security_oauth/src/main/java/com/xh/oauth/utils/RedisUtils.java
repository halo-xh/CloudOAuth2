package com.xh.oauth.utils;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

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


    public boolean set(String key, String value, long time) {
        try {
            if (time > 0L) {
                this.redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                 return this.set(key, value);
            }
            return true;
        } catch (Exception var6) {
            var6.printStackTrace();
            return false;
        }
    }

    public boolean set(String key, String value) {
        try {
            this.redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception var6) {
            var6.printStackTrace();
            return false;
        }
    }

    public boolean exist(String valueOf) {
        Boolean hasKey = redisTemplate.hasKey(valueOf);
        return Boolean.TRUE.equals(hasKey);
    }
}

