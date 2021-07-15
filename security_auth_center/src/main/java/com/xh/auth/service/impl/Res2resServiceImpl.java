package com.xh.auth.service.impl;

import com.xh.auth.domain.Res2res;
import com.xh.auth.repo.Res2ResRepository;
import com.xh.auth.service.Res2resService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * mapping table: role -function, function - resource 服务实现类
 * </p>
 *
 * @author Xiao Hong
 * @since 2020-12-10
 */
@Service
public class Res2resServiceImpl implements Res2resService {

    private final Res2ResRepository res2ResRepository;

    public Res2resServiceImpl(Res2ResRepository res2ResRepository) {
        this.res2ResRepository = res2ResRepository;
    }

    @Override
    public List<Res2res> findAll() {
        return res2ResRepository.findAll();
    }
}
