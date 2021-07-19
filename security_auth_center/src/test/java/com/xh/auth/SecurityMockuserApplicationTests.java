package com.xh.auth;

import com.xh.auth.domain.LoginToken;
import com.xh.auth.domain.SubjectLogin;
import com.xh.auth.repo.LoginTokenRepository;
import com.xh.auth.repo.SubjectLoginRepository;
import com.xh.common.utils.DeletedEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;

@SpringBootTest
class SecurityMockuserApplicationTests {

    @Autowired
    private LoginTokenRepository loginTokenRepository;

    @Autowired
    private SubjectLoginRepository subjectLoginRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void contextLoads() {
        LoginToken loginToken = new LoginToken();
        loginToken.setUserName("2");
        loginToken.setToken("1");
        loginToken.setVersion(1);
        loginToken.setExpireDate(new Date());
        loginTokenRepository.save(loginToken);
    }

    @Test
    void contextLoads1() {
        SubjectLogin subjectLogin = new SubjectLogin();
        subjectLogin.setCreateDate(new Date());
        subjectLogin.setDeleted(DeletedEnum.NO);
        subjectLogin.setLoginName("qwe");
        subjectLogin.setPassword(passwordEncoder.encode("123456"));
        subjectLogin.setStatus("A");
        subjectLoginRepository.saveAndFlush(subjectLogin);
    }


}
