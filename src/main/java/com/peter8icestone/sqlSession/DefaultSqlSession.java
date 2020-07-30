package com.peter8icestone.sqlSession;

import com.peter8icestone.pojo.Configuration;
import com.peter8icestone.pojo.MapperStatement;

import java.sql.SQLException;
import java.util.List;

public class DefaultSqlSession implements SqlSession {

    private Configuration configuration;

    public DefaultSqlSession(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public <E> List<E> selectList(String statementId, Object... params) {
        Executor simpleExecutor = new SimpleExecutor();
        MapperStatement mapperStatement = configuration.getMappedStatementMap().get(statementId);
        return simpleExecutor.query(configuration, mapperStatement, params);
    }

    @Override
    public <T> T selectOne(String statementId, Object... params) {
        List<Object> objects = selectList(statementId, params);
        if (objects.size() == 1) {
            return (T) objects.get(0);
        }
        throw new RuntimeException("get none or too much results");
    }
}
