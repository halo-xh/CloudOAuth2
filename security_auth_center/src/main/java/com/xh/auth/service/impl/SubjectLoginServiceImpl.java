package com.xh.auth.service.impl;

import com.xh.auth.domain.SubjectLogin;
import com.xh.auth.repo.SubjectLoginRepository;
import com.xh.auth.service.SubjectLoginService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Xiao Hong
 * @since 2020-12-09
 */
@Service
public class SubjectLoginServiceImpl implements SubjectLoginService {

    private final SubjectLoginRepository subjectLoginRepository;

    public SubjectLoginServiceImpl(SubjectLoginRepository subjectLoginRepository) {
        this.subjectLoginRepository = subjectLoginRepository;
    }

    @Override
    public SubjectLogin selectByLoginName(String username) {
        return subjectLoginRepository.findByLoginName(username);
    }
}
