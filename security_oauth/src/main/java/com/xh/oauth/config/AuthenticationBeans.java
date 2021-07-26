package com.xh.oauth.config;

import com.xh.oauth.clients.MyClientDetailsService;
import com.xh.oauth.clients.service.DClientDetailsService;
import com.xh.oauth.exception.AuthClientExceptionHandler;
import com.xh.oauth.token.MyAuthorizationCodeServices;
import com.xh.oauth.token.service.OAuthenticationStoreService;
import com.xh.oauth.utils.SnowflakeIdWorker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * author  Xiao Hong
 * date  2021/7/13 23:51
 * description
 */
@Configuration
public class AuthenticationBeans {

    // ========================================================
    // ==================CLIENTS CONFIGURATION=================
    // ========================================================

    /**
     * 密码解析器
     **/
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    /**
     * A service that provides the details about an OAuth2 client.
     *
     * @return service
     */
    @Bean(name = "myClientDetailsService")
    public MyClientDetailsService clientDetailsService(DClientDetailsService detailsService) {
        return new MyClientDetailsService(detailsService);
    }



    /**
     * Services for issuing and storing authorization codes.
     **/
    @Bean
    public MyAuthorizationCodeServices authorizationCodeServices(SnowflakeIdWorker idWorker, OAuthenticationStoreService authenticationStoreService) {
        return new MyAuthorizationCodeServices(authenticationStoreService, idWorker);
    }


    @Bean
    public AuthClientExceptionHandler authClientExceptionHandler() {
        return new AuthClientExceptionHandler();
    }

//    /**
//     * token 存储器
//     **/
//    @Bean
//    public TokenStore tokenStore(RedisConnectionFactory redisConnectionFactory) {
//        return new RedisTokenStore(redisConnectionFactory);
//    }


}
