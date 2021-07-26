package com.xh.oauth.clients;

import com.xh.oauth.clients.entity.ClientDetails;
import com.xh.oauth.exception.ClientAlreadyExistsException;
import com.xh.oauth.exception.NoSuchClientException;

import java.util.List;

public interface ClientRegistrationService {

    void addClientDetails(ClientDetails clientDetails) throws ClientAlreadyExistsException;

    void updateClientDetails(ClientDetails clientDetails) throws NoSuchClientException;

    void updateClientSecret(String clientId, String secret) throws NoSuchClientException;

    void removeClientDetails(String clientId) throws NoSuchClientException;

    List<ClientDetails> listClientDetails();

}
