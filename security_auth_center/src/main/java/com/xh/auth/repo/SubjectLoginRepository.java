package com.xh.auth.repo;

import com.xh.auth.domain.SubjectLogin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectLoginRepository extends JpaRepository<SubjectLogin, Long>, JpaSpecificationExecutor<SubjectLogin> {

    SubjectLogin findByLoginName(String username);

}
