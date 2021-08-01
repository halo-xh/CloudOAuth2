package com.xh.thirdclient.utils;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * author  Xiao Hong
 * date  2021/7/14 21:15
 * description
 */
@Slf4j
@Component
public class IdCreator implements IdentifierGenerator {

    private static final SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker();

    @Override
    public Serializable generate(SharedSessionContractImplementor implementor, Object o) throws HibernateException {
        long id = snowflakeIdWorker.nextId();
        log.debug("create id -> [{}]", id);
        return id;
    }

    @Override
    public boolean supportsJdbcBatchInserts() {
        return true;
    }
}
