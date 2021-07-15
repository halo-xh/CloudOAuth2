package com.xh.auth.repo;

import com.xh.auth.domain.Res2res;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface Res2ResRepository extends JpaRepository<Res2res, Long>, JpaSpecificationExecutor<Res2res> {
}
