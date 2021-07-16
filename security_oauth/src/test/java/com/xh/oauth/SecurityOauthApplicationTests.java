package com.xh.oauth;

import com.xh.oauth.clients.entity.MyClientDetails;
import com.xh.oauth.clients.repo.MyClientDetailsRepository;
import com.xh.oauth.utils.YesOrNoEnum;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class SecurityOauthApplicationTests {

    @Autowired
    private MyClientDetailsRepository clientDetailsRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void contextLoads() {
        String admin = passwordEncoder.encode("admin");
        MyClientDetails myClientDetails = new MyClientDetails();
        myClientDetails.setClientSecret(admin);
        myClientDetails.setAuthorities("13,123,123");
        myClientDetails.setClientId("admin");
        myClientDetails.setAutoApproveScopes(YesOrNoEnum.NO);
        myClientDetails.setAccessTokenValiditySeconds(10000);
        myClientDetails.setScope("test");
        myClientDetails.setRefreshTokenValiditySeconds(1000);
        myClientDetails.setRegisteredRedirectUris("www.baidu.com");
        myClientDetails.setAuthorizedGrantTypes("authorization_code");
        myClientDetails.setResourceIds("2131");
        clientDetailsRepository.save(myClientDetails);

    }

}
