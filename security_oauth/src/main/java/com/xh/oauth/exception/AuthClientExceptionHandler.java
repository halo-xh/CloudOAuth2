package com.xh.oauth.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * author  Xiao Hong
 * date  2021/7/14 0:10
 * description
 */
public class AuthClientExceptionHandler implements AuthenticationEntryPoint {

    private final Logger logger = LoggerFactory.getLogger(AuthClientExceptionHandler.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         org.springframework.security.core.AuthenticationException authException) throws IOException {
        logger.error("客户端认证异常:{}", authException.getMessage());
        Throwable cause = authException.getCause();
        if (cause instanceof InvalidTokenException) {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "Invalid Token");
        } else {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "认证失败");
        }

    }
}
