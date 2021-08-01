package com.xh.source.service;

import com.xh.source.entity.TcAuthor;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xh.source.event.SaveEvent;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author xh
 * @since 2021-07-31
 */
public interface AuthorService extends IService<TcAuthor> {

    List<TcAuthor> getList();

    boolean create(TcAuthor author);

    int saveAnother(SaveEvent saveEvent);
}
