package com.xh.oauth.token.repo;

import com.xh.oauth.token.entity.OAuthCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface OAuthenticationStoreRepository extends JpaRepository<OAuthCode, String>, JpaSpecificationExecutor<OAuthCode> {
}
