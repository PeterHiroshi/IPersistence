package com.peter8icestone.sqlSession;

import com.peter8icestone.pojo.Configuration;
import com.peter8icestone.pojo.MapperStatement;

import java.util.List;

public class SimpleExecutor implements Executor {

    @Override
    public <E> List<E> query(Configuration configuration, MapperStatement mapperStatement, Object... params) {
        return null;
    }
}
