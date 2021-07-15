package com.xh.auth.repo;

import com.xh.auth.domain.Resources;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResourcesRepository extends JpaRepository<Resources, Long>, JpaSpecificationExecutor<Resources> {

    List<Resources> findAllByResType(String resTypeApi);
}
