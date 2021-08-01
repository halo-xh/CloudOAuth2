package com.xh.thirdclient.service;

import com.xh.thirdclient.domain.Author;
import com.xh.thirdclient.event.SaveEvent;

import java.util.List;

/**
 * author  Xiao Hong
 * date  2021/7/30 23:49
 * description
 */

public interface AuthorService {

    List<Author> getList();

    Author create(Author author);

    Author saveAnother(SaveEvent saveEvent);
}
