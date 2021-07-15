package com.xh.auth.security;

import com.xh.auth.domain.SubjectLogin;
import com.xh.auth.service.SubjectLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Created by Xiao Hong on 2020-12-10
 */
@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private SubjectLoginService subjectLoginService;

    @Override
    public LoginUser loadUserByUsername(String username) throws UsernameNotFoundException {
        SubjectLogin subjectLogin = subjectLoginService.selectByLoginName(username);
        if (subjectLogin != null) {
            LoginUser loginUser = new LoginUser();
            loginUser.setLoginname(subjectLogin.getLoginName());
            loginUser.setPassword(subjectLogin.getPassword());
            loginUser.setSid(subjectLogin.getSid());
            loginUser.setStatus(subjectLogin.getStatus());
            return loginUser;
        } else {
            throw new UsernameNotFoundException("UserDetailsService.userNotFound");
        }
    }


}
