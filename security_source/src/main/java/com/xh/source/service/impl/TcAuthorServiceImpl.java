package com.xh.source.service.impl;

import com.xh.source.entity.TcAuthor;
import com.xh.source.event.SaveEvent;
import com.xh.source.mapper.TcAuthorMapper;
import com.xh.source.service.AuthorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author xh
 * @since 2021-07-31
 */
@Service
public class TcAuthorServiceImpl extends ServiceImpl<TcAuthorMapper, TcAuthor> implements AuthorService {

    @Resource
    private TcAuthorMapper tcAuthorMapper;

    @Override
    public List<TcAuthor> getList() {
        return tcAuthorMapper.selectList(null);
    }

    @Override
    public boolean create(TcAuthor author) {
        return save(author);
    }

    @Override
    public int saveAnother(SaveEvent saveEvent) {
//        TcAuthor tcAuthor = tcAuthorMapper.selectById(saveEvent.getAid());
        TcAuthor tcAuthor1 = new TcAuthor();
        tcAuthor1.setAge(888);
        tcAuthor1.setName(saveEvent.getName());
        super.save(tcAuthor1);
        return 0;
    }
}
