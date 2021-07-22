package com.xh.oauth.user;

import com.xh.common.domains.LoginVM;
import com.xh.common.feign.AuthFeign;
import com.xh.oauth.user.entity.Subject;
import com.xh.oauth.user.repo.SubjectRepository;
import com.xh.oauth.user.service.ExtUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by Xiao Hong on 2020-12-10
 */
@Service
@Primary
public class ExtUserDetailsServiceImpl implements ExtUserDetailsService {

    private final SubjectRepository subjectRepository;

    @Resource
    private AuthFeign authFeign;

    public ExtUserDetailsServiceImpl(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    @Override
    public LoginUser loadUserByUsername(String username) throws UsernameNotFoundException {
        Subject subject = subjectRepository.findByLoginName(username);
        if (subject != null) {
            LoginUser loginUser = new LoginUser();
            loginUser.setLoginname(subject.getLoginName());
            loginUser.setPassword(subject.getPassword());
            loginUser.setSid(subject.getSid());
            loginUser.setStatus(subject.getStatus());
            return loginUser;
        } else {
            throw new UsernameNotFoundException("UserDetailsService.userNotFound");
        }
    }


}
