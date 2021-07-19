package com.xh.oauth2.config;

import com.xh.common.domains.SubjectLogin;
import com.xh.common.feign.AuthFeign;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author xiaohong
 * @version 1.0
 * @date 2021/7/19 14:01
 * @description
 */
@Service
public class CustomUserDetailService implements UserDetailsService {

    @Resource
    private AuthFeign feign;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SubjectLogin subjectLogin = feign.findByName(username);
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
