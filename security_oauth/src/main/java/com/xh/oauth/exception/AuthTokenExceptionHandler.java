package com.xh.oauth.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.stereotype.Component;

/**
 * author  Xiao Hong
 * date  2021/7/13 23:43
 * description 认证失败；密码错误
 */
@Slf4j
public class AuthTokenExceptionHandler implements WebResponseExceptionTranslator<OAuth2Exception> {

    @Override
    public ResponseEntity<OAuth2Exception> translate(Exception e) throws Exception {
        log.error("授权失败: ", e);
        return ResponseEntity.ok((OAuth2Exception) e);
    }

}
