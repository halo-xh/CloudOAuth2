package com.xh.source.support;

import com.xh.source.entity.TcAuthor;
import com.xh.source.event.SaveEvent;
import com.xh.source.service.AuthorService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * author  Xiao Hong
 * date  2021/7/31 9:45
 * description
 */
@Service
@Transactional
public class AuthorSupportImpl implements AuthorSupport {

    private final AuthorService authorService;

    private final ApplicationEventPublisher publisher;

    public AuthorSupportImpl(AuthorService authorService, ApplicationEventPublisher publisher) {
        this.authorService = authorService;
        this.publisher = publisher;
    }

    @Override
    public List<TcAuthor> getList() {
        return authorService.getList();
    }

    @Override
    public TcAuthor create(TcAuthor author) {
        boolean c = authorService.create(author);
        SaveEvent saveEvent = new SaveEvent();
        saveEvent.setAid(author.getAid());
        saveEvent.setName(author.getName());
        publisher.publishEvent(saveEvent);
        return author;
    }

    @Override
    public void saveAnother(SaveEvent saveEvent) {

    }
}
