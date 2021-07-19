package com.xh.oauth.token;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xh.oauth.endpoints.request.FirstAuthorizationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.common.util.OAuth2Utils;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * author  Xiao Hong
 * date  2021/7/17 14:26
 * description client 信息验证通过生成的临时token，用于用户通过验证时校验用。
 */
@Component
public class RedisClientTokenProvider implements ClientTokenProvider {

    private final Logger logger = LoggerFactory.getLogger(RedisClientTokenProvider.class);

    private final RedisTemplate<String, String> redisTemplate;

    public RedisClientTokenProvider(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public FirstAuthorizationRequest validateToken(String temTokenKey) {
        String val = redisTemplate.opsForValue().get(temTokenKey);
        if (val != null) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(val);
                return buildFirstAuthRequest(rootNode);
            } catch (JsonProcessingException e) {
                logger.error("parse token from str error...");
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public String storeToken(FirstAuthorizationRequest authorizationRequest) {
        String randomKey = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(randomKey, authorizationRequest.toString(), 30, TimeUnit.SECONDS);
        return randomKey;
    }

    private FirstAuthorizationRequest buildFirstAuthRequest(JsonNode parameters) {
        FirstAuthorizationRequest firstAuthorizationRequest = new FirstAuthorizationRequest();
        firstAuthorizationRequest.setClientId(parameters.get(OAuth2Utils.CLIENT_ID).asText());
        firstAuthorizationRequest.setRedirectUri(parameters.get(OAuth2Utils.REDIRECT_URI).asText());
        firstAuthorizationRequest.setResponseType(parameters.get(OAuth2Utils.RESPONSE_TYPE).asText());
        firstAuthorizationRequest.setState(parameters.get(OAuth2Utils.STATE).asText());
        return firstAuthorizationRequest;
    }


}
