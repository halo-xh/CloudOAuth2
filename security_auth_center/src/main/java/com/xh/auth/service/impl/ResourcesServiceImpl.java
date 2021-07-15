package com.xh.auth.service.impl;

import com.xh.auth.domain.Resources;
import com.xh.auth.repo.ResourcesRepository;
import com.xh.auth.service.ResourcesService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Xiao Hong
 * @since 2020-12-09
 */
@Service
public class ResourcesServiceImpl implements ResourcesService {

    private final ResourcesRepository resourcesRepository;

    public ResourcesServiceImpl(ResourcesRepository resourcesRepository) {
        this.resourcesRepository = resourcesRepository;
    }

    @Override
    public List<Resources> findAllByResType(String resType) {
        return resourcesRepository.findAllByResType(resType);
    }
}
