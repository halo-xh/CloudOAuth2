package com.xh.oauth.token.service;

import com.xh.oauth.token.entity.OAuthStore;
import com.xh.oauth.token.repo.OAuthenticationStoreRepository;
import com.xh.oauth.utils.DeletedEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * author  Xiao Hong
 * date  2021/7/14 23:47
 * description
 */
@Slf4j
@Service
public class OAuthenticationStoreServiceImpl implements OAuthenticationStoreService {

    private final OAuthenticationStoreRepository authenticationStoreRepository;

    public OAuthenticationStoreServiceImpl(OAuthenticationStoreRepository authenticationStoreRepository) {
        this.authenticationStoreRepository = authenticationStoreRepository;
    }

    public OAuthStore save(OAuthStore authStore) {
        return authenticationStoreRepository.save(authStore);
    }

    @Override
    public OAuthStore remove(String code) {
        Optional<OAuthStore> authStore = authenticationStoreRepository.findById(code);
        boolean present = authStore.isPresent();
        if (present) {
            OAuthStore store = authStore.get();
            store.setDeleted(DeletedEnum.YES);
            return authenticationStoreRepository.save(store);
        }
        return null;
    }
}
