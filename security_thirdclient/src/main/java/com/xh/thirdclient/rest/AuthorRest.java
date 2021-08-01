package com.xh.thirdclient.rest;

import com.xh.thirdclient.domain.Author;
import com.xh.thirdclient.support.AuthorSupport;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * author  Xiao Hong
 * date  2021/7/30 23:50
 * description
 */
@RestController
@RequestMapping("/author")
public class AuthorRest {


    private final AuthorSupport authorSupport;

    public AuthorRest(AuthorSupport authorSupport) {
        this.authorSupport = authorSupport;
    }


    @GetMapping("/list")
    public List<Author> getList() {
        return authorSupport.getList();
    }

    @PostMapping("/create")
    public Author create(@RequestBody Author author) {
        return authorSupport.create(author);
    }
}
