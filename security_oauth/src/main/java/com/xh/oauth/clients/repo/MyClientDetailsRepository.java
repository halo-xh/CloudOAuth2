package com.xh.oauth.clients.repo;

import com.xh.oauth.clients.entity.MyClientDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * @author xiaohong
 * @version 1.0
 * @date 2021/7/14 10:00
 * @description
 */
@Repository
public interface MyClientDetailsRepository extends JpaRepository<MyClientDetails, String>, JpaSpecificationExecutor<MyClientDetails> {

}
