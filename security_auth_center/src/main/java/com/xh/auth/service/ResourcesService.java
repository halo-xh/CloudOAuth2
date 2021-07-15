package com.xh.auth.service;

import com.xh.auth.domain.Resources;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Xiao Hong
 * @since 2020-12-09
 */
public interface ResourcesService {

    List<Resources> findAllByResType(String resType);
}
