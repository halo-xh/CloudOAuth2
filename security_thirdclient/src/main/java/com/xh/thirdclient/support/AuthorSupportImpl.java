package com.xh.thirdclient.support;

import com.xh.thirdclient.domain.Author;
import com.xh.thirdclient.event.SaveEvent;
import com.xh.thirdclient.event.SaveEventListener;
import com.xh.thirdclient.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * author  Xiao Hong
 * date  2021/7/31 9:45
 * description
 */
@Component
//@Transactional
public class AuthorSupportImpl implements AuthorSupport {

    private final AuthorService authorService;

    private final ApplicationEventPublisher publisher;

    public AuthorSupportImpl(AuthorService authorService, ApplicationEventPublisher publisher) {
        this.authorService = authorService;
        this.publisher = publisher;
    }

    @Override
    public List<Author> getList() {
        return authorService.getList();
    }

    @Override
    public Author create(Author author) {
        Author author1 = authorService.create(author);
        SaveEvent saveEvent = new SaveEvent();
        saveEvent.setAid(author1.getAid());
        saveEvent.setName(author1.getName() + "Event");
        publisher.publishEvent(saveEvent);
        return author1;
    }

    @Override
    public void saveAnother(SaveEvent saveEvent) {

    }
}
