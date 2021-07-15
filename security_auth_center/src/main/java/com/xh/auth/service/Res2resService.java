package com.xh.auth.service;

import com.xh.auth.domain.Res2res;

import java.util.List;

/**
 * <p>
 * mapping table: role -function, function - resource 服务类
 * </p>
 *
 * @author Xiao Hong
 * @since 2020-12-10
 */
public interface Res2resService {

    List<Res2res> findAll();

}
