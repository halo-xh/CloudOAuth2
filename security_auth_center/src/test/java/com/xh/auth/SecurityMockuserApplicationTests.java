package com.xh.auth;

import com.xh.auth.domain.LoginToken;
import com.xh.auth.repo.LoginTokenRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
class SecurityMockuserApplicationTests {

    @Autowired
    private LoginTokenRepository loginTokenRepository;

    @Test
    void contextLoads() {
        LoginToken loginToken = new LoginToken();
        loginToken.setUserName("2");
        loginToken.setToken("1");
        loginToken.setVersion(1);
        loginToken.setExpireDate(new Date());
        loginTokenRepository.save(loginToken);
    }

}
