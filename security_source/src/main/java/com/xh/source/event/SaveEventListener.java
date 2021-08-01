package com.xh.source.event;

import com.xh.source.service.AuthorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * author  Xiao Hong
 * date  2021/7/31 9:42
 * description
 */
@Slf4j
@Component
public class SaveEventListener {

    private final AuthorService authorService;

    public SaveEventListener(AuthorService authorService) {
        this.authorService = authorService;
    }

    @TransactionalEventListener
    public void listen(@Payload SaveEvent saveEvent) {
        int author = authorService.saveAnother(saveEvent);
        log.info("saved author,{}", author);
    }
}
