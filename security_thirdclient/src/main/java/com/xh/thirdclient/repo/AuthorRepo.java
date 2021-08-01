package com.xh.thirdclient.repo;

import com.xh.thirdclient.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * author  Xiao Hong
 * date  2021/7/30 23:48
 * description
 */
@Repository
public interface AuthorRepo extends JpaRepository<Author, Long>, JpaSpecificationExecutor<Author> {

}
