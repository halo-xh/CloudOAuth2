package com.xh.auth.repo;

import com.xh.auth.domain.User2role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface User2RoleRepository extends JpaRepository<User2role, Long>, JpaSpecificationExecutor<User2role> {

    List<User2role> findAllByUserId(Long userId);

}
