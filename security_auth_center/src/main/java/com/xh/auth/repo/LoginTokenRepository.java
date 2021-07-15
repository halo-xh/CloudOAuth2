package com.xh.auth.repo;

import com.xh.auth.domain.LoginToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginTokenRepository extends JpaRepository<LoginToken, Long>, JpaSpecificationExecutor<LoginToken> {

    LoginToken findByUserNameAndToken(String username, String authToken);

    LoginToken findByUserName(String username);
}
