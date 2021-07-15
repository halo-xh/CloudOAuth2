package com.xh.auth.service.impl;

import com.xh.auth.domain.User2role;
import com.xh.auth.repo.User2RoleRepository;
import com.xh.auth.service.User2roleService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * user mapping role. 服务实现类
 * </p>
 *
 * @author Xiao Hong
 * @since 2020-12-10
 */
@Service
public class User2roleServiceImpl implements User2roleService {

    private final User2RoleRepository user2RoleRepository;

    public User2roleServiceImpl(User2RoleRepository user2RoleRepository) {
        this.user2RoleRepository = user2RoleRepository;
    }

    @Override
    public List<User2role> findByUserId(Integer userId) {
        return user2RoleRepository.findAllByUserId(userId);
    }
}
