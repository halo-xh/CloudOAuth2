package com.xh.oauth.clients.service;

import com.xh.oauth.clients.entity.MyClientDetails;
import com.xh.oauth.clients.repo.MyClientDetailsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * author  Xiao Hong
 * date  2021/7/14 23:58
 * description
 */
@Slf4j
@Service
public class DClientDetailsServiceImpl implements DClientDetailsService {

    private final MyClientDetailsRepository clientDetailsRepository;

    public DClientDetailsServiceImpl(MyClientDetailsRepository clientDetailsRepository) {
        this.clientDetailsRepository = clientDetailsRepository;
    }

    @Override
    public Optional<MyClientDetails> findById(String clientId) {
        return clientDetailsRepository.findById(clientId);
    }

    @Override
    public void saveAndFlush(MyClientDetails clientDetails) {
        clientDetailsRepository.saveAndFlush(clientDetails);
    }

    @Override
    public void save(MyClientDetails myClientDetails) {
        clientDetailsRepository.saveAndFlush(myClientDetails);
    }

    @Override
    public void deleteById(String clientId) {
        clientDetailsRepository.deleteById(clientId);
    }

    @Override
    public List<MyClientDetails> findAll() {
        return clientDetailsRepository.findAll();
    }
}
