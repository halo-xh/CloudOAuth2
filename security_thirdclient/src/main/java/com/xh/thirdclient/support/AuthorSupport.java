package com.xh.thirdclient.support;

import com.xh.thirdclient.domain.Author;
import com.xh.thirdclient.event.SaveEvent;
import com.xh.thirdclient.service.AuthorService;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * author  Xiao Hong
 * date  2021/7/31 9:45
 * description
 */
public interface AuthorSupport {

    List<Author> getList();

    Author create(Author author);

    void saveAnother(SaveEvent saveEvent);

}

