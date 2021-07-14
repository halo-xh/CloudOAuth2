package com.xh.oauth.clients;

import com.xh.oauth.clients.entity.MyClientDetails;
import com.xh.oauth.clients.repo.MyClientDetailsRepository;
import org.springframework.security.oauth2.provider.*;

import java.util.ArrayList;
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

    private MyClientDetailsRepository clientDetailsRepository;

    public MyClientDetailsService(MyClientDetailsRepository myClientDetailsRepository) {
        this.clientDetailsRepository = myClientDetailsRepository;
    }

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        return clientDetailsRepository.findById(clientId).orElse(null);
    }

    @Override
    public void addClientDetails(ClientDetails clientDetails) throws ClientAlreadyExistsException {
        clientDetailsRepository.saveAndFlush((MyClientDetails) clientDetails);
    }

    @Override
    public void updateClientDetails(ClientDetails clientDetails) throws NoSuchClientException {
        clientDetailsRepository.saveAndFlush((MyClientDetails) clientDetails);
    }

    @Override
    public void updateClientSecret(String clientId, String secret) throws NoSuchClientException {
        Optional<MyClientDetails> details = clientDetailsRepository.findById(clientId);
        if (details.isPresent()) {
            MyClientDetails myClientDetails = details.get();
            myClientDetails.setClientSecret(secret);
            clientDetailsRepository.save(myClientDetails);
        }
    }

    @Override
    public void removeClientDetails(String clientId) throws NoSuchClientException {
        clientDetailsRepository.deleteById(clientId);
    }

    @Override
    public List<ClientDetails> listClientDetails() {
        return new LinkedList<>(clientDetailsRepository.findAll());
    }
}
