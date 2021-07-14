package com.xh.oauth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xiaohong
 * @version 1.0
 * @date 2021/7/14 11:19
 * @description
 */
@Service
public class MyUserDetailServiceImpl implements UserDetailsService {

    private final Logger logger = LoggerFactory.getLogger(MyUserDetailServiceImpl.class);

    private final PasswordEncoder passwordEncoder;

    public MyUserDetailServiceImpl(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String usernameFx = "found";

        logger.info("用户身份验证...");
        if (!username.equals(usernameFx)) {
            logger.error("用户不存在!");
            throw new UsernameNotFoundException(usernameFx);
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("admin"));
        String password = passwordEncoder.encode("123456");
        return new User(usernameFx, password, true, true, true, true, authorities);

    }
}
