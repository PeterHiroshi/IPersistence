package com.peter8icestone.sqlSession;

import java.sql.SQLException;
import java.util.List;

public interface SqlSession {

    <E> List<E> selectList(String statementId, Object... params);

    <T> T selectOne(String statementId, Object... params);

    /**
     * return proxy instance of Dao Interface
     * @param mapperClass Dao Interface class
     * @param <T> specific class type
     * @return proxy instance of Dao
     */
    <T> T getMapper(Class mapperClass);
}
