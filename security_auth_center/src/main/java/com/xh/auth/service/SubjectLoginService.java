package com.xh.auth.service;


import com.xh.auth.domain.SubjectLogin;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Xiao Hong
 * @since 2020-12-09
 */
public interface SubjectLoginService {

    SubjectLogin selectByLoginName(String username);
}
