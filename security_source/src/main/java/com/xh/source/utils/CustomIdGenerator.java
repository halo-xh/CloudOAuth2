package com.xh.source.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;

import java.io.Serializable;

/**
 * author  Xiao Hong
 * date  2021/7/14 21:15
 * description
 */
@Slf4j
@Component
public class CustomIdGenerator implements IdentifierGenerator {

    @Autowired
    private SnowflakeIdWorker snowflakeIdWorker;

    @Override
    public Long nextId(Object entity) {
        //可以将当前传入的class全类名来作为bizKey,或者提取参数来生成bizKey进行分布式Id调用生成.
        String bizKey = entity.getClass().getName();
        //根据bizKey调用分布式ID生成
        long id = snowflakeIdWorker.nextId();
        //返回生成的id值即可.
        return id;
    }

}
