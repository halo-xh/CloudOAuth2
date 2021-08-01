package com.xh.oauth.token.service;

import com.xh.oauth.token.entity.OAuthCode;
import com.xh.oauth.token.repo.OAuthenticationStoreRepository;
import com.xh.common.enums.YesOrNoEnum;
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

    @Override
    public OAuthCode save(OAuthCode authStore) {
        return authenticationStoreRepository.save(authStore);
    }

    @Override
    public OAuthCode remove(String code) {
        Optional<OAuthCode> authStore = authenticationStoreRepository.findById(code);
        boolean present = authStore.isPresent();
        if (present) {
            OAuthCode store = authStore.get();
            store.setDeleted(YesOrNoEnum.YES);
            return authenticationStoreRepository.save(store);
        }
        return null;
    }
}
