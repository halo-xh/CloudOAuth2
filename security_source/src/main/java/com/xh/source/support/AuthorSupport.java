package com.xh.source.support;

import com.xh.source.entity.TcAuthor;
import com.xh.source.event.SaveEvent;

import java.util.List;

/**
 * author  Xiao Hong
 * date  2021/7/31 9:45
 * description
 */
public interface AuthorSupport {

    List<TcAuthor> getList();

    TcAuthor create(TcAuthor author);

    void saveAnother(SaveEvent saveEvent);

}

