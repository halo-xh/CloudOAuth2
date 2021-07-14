package com.xh.oauth.user;

import com.xh.oauth.user.entity.Subject;
import com.xh.oauth.user.repo.SubjectRepository;
import com.xh.oauth.user.service.ExtUserDetailsService;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Created by Xiao Hong on 2020-12-10
 */
@Service
@Primary
public class ExtUserDetailsServiceImpl implements ExtUserDetailsService {

    private final SubjectRepository subjectRepository;

    public ExtUserDetailsServiceImpl(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    @Override
    public LoginUser loadUserByUsername(String username) throws UsernameNotFoundException {
        Subject subject = subjectRepository.selectByLoginName(username);
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
