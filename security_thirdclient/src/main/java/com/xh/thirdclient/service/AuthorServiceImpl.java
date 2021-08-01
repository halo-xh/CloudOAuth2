package com.xh.thirdclient.service;

import com.xh.thirdclient.domain.Author;
import com.xh.thirdclient.event.SaveEvent;
import com.xh.thirdclient.repo.AuthorRepo;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * author  Xiao Hong
 * date  2021/7/30 23:50
 * description
 */
@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepo authorRepo;

    private final ApplicationEventPublisher applicationEventPublisher;

    public AuthorServiceImpl(AuthorRepo authorRepo, ApplicationEventPublisher applicationEventPublisher) {
        this.authorRepo = authorRepo;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public List<Author> getList() {
        return authorRepo.findAll();
    }

    @Override
    public Author create(Author author) {
        Author save = authorRepo.save(author);
        return save;
    }

    @Override
    public Author saveAnother(SaveEvent saveEvent) {
        Author one = authorRepo.getOne(saveEvent.getAid());
        Author author = new Author();
        author.setName(one.getName());
        author.setAge(one.getAge() + 1);
        Author save = authorRepo.save(author);
        return save;
    }

}
