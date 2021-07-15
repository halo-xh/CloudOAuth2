package com.xh.oauth.clients;

import com.xh.oauth.clients.entity.MyClientDetails;
import com.xh.oauth.clients.service.DClientDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * @author xiaohong
 * @version 1.0
 * @date 2021/7/14 10:00
 * @description
 */
public class MyClientDetailsService implements ClientDetailsService, ClientRegistrationService {

    private final DClientDetailsService detailsService;

    private  PasswordEncoder passwordEncoder;

    public MyClientDetailsService(DClientDetailsService detailsService) {
        this.detailsService = detailsService;
    }

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        return detailsService.findById(clientId).orElse(null);
    }


    @Override
    public void addClientDetails(ClientDetails clientDetails) throws ClientAlreadyExistsException {
        detailsService.saveAndFlush((MyClientDetails) clientDetails);
    }

    @Override
    public void updateClientDetails(ClientDetails clientDetails) throws NoSuchClientException {
        detailsService.saveAndFlush((MyClientDetails) clientDetails);
    }

    @Override
    public void updateClientSecret(String clientId, String secret) throws NoSuchClientException {
        Optional<MyClientDetails> details = detailsService.findById(clientId);
        if (details.isPresent()) {
            MyClientDetails myClientDetails = details.get();
            String encode = passwordEncoder.encode(secret);
            myClientDetails.setClientSecret(encode);
            detailsService.save(myClientDetails);
        }
    }

    @Override
    public void removeClientDetails(String clientId) throws NoSuchClientException {
        detailsService.deleteById(clientId);
    }

    @Override
    public List<ClientDetails> listClientDetails() {
        List<MyClientDetails> all = detailsService.findAll();
        return new LinkedList<>(all);
    }

    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
}
