package com.peter8icestone.sqlSession;

import com.peter8icestone.pojo.Configuration;
import com.peter8icestone.pojo.MapperStatement;

import java.sql.SQLException;
import java.util.List;

public interface Executor {

    <E> List<E> query(MapperStatement mapperStatement, Object... params);

    int update(MapperStatement mapperStatement, Object... param);
}
