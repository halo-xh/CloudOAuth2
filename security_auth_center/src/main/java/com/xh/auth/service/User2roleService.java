package com.xh.auth.service;


import com.xh.auth.domain.User2role;

import java.util.List;

/**
 * <p>
 * user mapping role. 服务类
 * </p>
 *
 * @author Xiao Hong
 * @since 2020-12-10
 */
public interface User2roleService {

    List<User2role> findByUserId(Long userId);
}
