package com.peter8icestone.sqlSession;

import com.peter8icestone.pojo.Configuration;
import com.peter8icestone.pojo.MapperStatement;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
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

    @Override
    public <T> T getMapper(Class mapperClass) {
        // execute JDBC
        // new jdk proxy instance
        Object proxyInstance = Proxy.newProxyInstance(mapperClass.getClassLoader(), new Class<?>[]{mapperClass}, (proxy, method, args) -> {
            // generate statementId
            String methodName = method.getName();
            String className = method.getDeclaringClass().getName();
            String statementId = className + "." + methodName;
            // automatically call selectOne() or selectList by condition
            Type genericReturnType = method.getGenericReturnType();
            // 判断是否实现 泛型类型参数化
            if (genericReturnType instanceof ParameterizedType) {
                return selectList(statementId, args);
            }
            return selectOne(statementId, args);
        });
        return (T) proxyInstance;
    }
}
