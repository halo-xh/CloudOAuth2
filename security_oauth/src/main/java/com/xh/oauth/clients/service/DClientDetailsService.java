package com.xh.oauth.clients.service;

import com.xh.oauth.clients.entity.MyClientDetails;
import org.springframework.security.oauth2.provider.ClientDetails;

import java.util.List;
import java.util.Optional;


public interface DClientDetailsService {

    Optional<MyClientDetails> findById(String clientId);

    void saveAndFlush(MyClientDetails clientDetails);

    void save(MyClientDetails myClientDetails);

    void deleteById(String clientId);

    List<MyClientDetails> findAll();

}
