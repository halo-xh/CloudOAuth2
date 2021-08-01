package com.xh.source.controller;


import com.xh.source.entity.TcAuthor;
import com.xh.source.support.AuthorSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author xh
 * @since 2021-07-31
 */
@RestController
@RequestMapping("/author")
public class TcAuthorController {

    @Autowired
    private AuthorSupport authorSupport;

    @GetMapping("/list")
    public List<TcAuthor> getList() {
        return authorSupport.getList();
    }

    @PostMapping("/create")
    public TcAuthor create(@RequestBody TcAuthor author) {
        return authorSupport.create(author);
    }
}
