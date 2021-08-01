package com.xh.thirdclient.event;

import com.xh.thirdclient.domain.Author;
import com.xh.thirdclient.service.AuthorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * author  Xiao Hong
 * date  2021/7/31 9:42
 * description
 */
@Slf4j
@Component
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class SaveEventListener {

    private final AuthorService authorService;

    public SaveEventListener(AuthorService authorService) {
        this.authorService = authorService;
    }

    @TransactionalEventListener
    public void listen(@Payload SaveEvent saveEvent) {
        Author author = authorService.saveAnother(saveEvent);
        log.info("saved author,{}", author);
    }
}
