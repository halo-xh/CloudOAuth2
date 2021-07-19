package com.xh.auth.rest;

import com.xh.auth.domain.SubjectLogin;
import com.xh.auth.service.SubjectLoginService;
import org.springframework.web.bind.annotation.*;

/**
 * @author xiaohong
 * @version 1.0
 * @date 2021/7/19 14:24
 * @description
 */
@RestController
@RequestMapping("/api/subject")
public class SubjectController {

    private final SubjectLoginService subjectLoginService;

    public SubjectController(SubjectLoginService subjectLoginService) {
        this.subjectLoginService = subjectLoginService;
    }

    @PostMapping("/findByName")
    public SubjectLogin getSubjectLogin(@RequestBody String userName){
        return subjectLoginService.selectByLoginName(userName);
    }
}
