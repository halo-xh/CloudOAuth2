package com.xh.auth.rest;

import com.xh.auth.domain.SubjectLogin;
import com.xh.auth.jwt.TokenProvider;
import com.xh.auth.service.SubjectLoginService;
import com.xh.common.annotation.AnonymousAccess;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xiaohong
 * @version 1.0
 * @date 2021/7/20 14:23
 * @description
 */
@RestController
@RequestMapping("/api/security")
public class SecurityController {

    private final TokenProvider tokenProvider;

    private final SubjectLoginService subjectLoginService;

    public SecurityController(TokenProvider tokenProvider, SubjectLoginService subjectLoginService) {
        this.tokenProvider = tokenProvider;
        this.subjectLoginService = subjectLoginService;
    }

    @AnonymousAccess
    @PostMapping("/validateToken")
    public SubjectLogin getSubjectLogin(@RequestBody String token){
        Authentication authentication = tokenProvider.getAuthentication(token);
        User user = (User) authentication.getPrincipal();
        String username = user.getUsername();
        return subjectLoginService.selectByLoginName(username);
    }
}
