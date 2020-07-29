package com.peter8icestone.sqlSession;

import java.util.List;

public interface SqlSession {

    public <E> List<E> selectList(String statementId, Object... params);

    public <T> T selectOne(String statementId, Object... params);
}
