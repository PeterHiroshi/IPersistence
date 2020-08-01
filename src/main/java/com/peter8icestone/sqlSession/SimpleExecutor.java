package com.peter8icestone.sqlSession;

import com.peter8icestone.config.BoundSql;
import com.peter8icestone.pojo.Configuration;
import com.peter8icestone.pojo.MapperStatement;
import com.peter8icestone.utils.GenericTokenParser;
import com.peter8icestone.utils.ParameterMapping;
import com.peter8icestone.utils.ParameterMappingTokenHandler;
import com.peter8icestone.utils.TokenHandler;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SimpleExecutor implements Executor {

    @Override
    public <E> List<E> query(Configuration configuration, MapperStatement mapperStatement, Object... params) {
        // register driver and get connection
        Connection connection = null;
        try {
            connection = configuration.getDataSource().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // get sql
        String sql = mapperStatement.getSql();
        // parse sql
        BoundSql boundSql = getBoundSql(sql);
        // get preparedStatement
        PreparedStatement preparedStatement = Optional.ofNullable(connection)
                .map(conn -> {
                    try {
                        return conn.prepareStatement(boundSql.getSqlText());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    return null;
                }).orElse(null);
        // set parameters
        // get the Class object of parameter's type
        String parameterTypeStr = mapperStatement.getParameterType();
        Class<?> parameterTypeClass = getClassType(parameterTypeStr);
        List<ParameterMapping> parameterMappingList = boundSql.getParameterMappingList();
        for (int i = 0; i < parameterMappingList.size(); i++) {
            ParameterMapping parameterMapping = parameterMappingList.get(i);
            String content = parameterMapping.getContent();
            // reflection
            Field declaredField = null;
            try {
                declaredField = parameterTypeClass.getDeclaredField(content);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
            if (declaredField == null) {
                continue;
            }
            declaredField.setAccessible(true);
            Object paramObj = null;
            try {
                paramObj = declaredField.get(params[0]);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            int paramIdx = i + 1;
            Optional.ofNullable(paramObj)
                    .ifPresent(obj -> {
                        try {
                            assert preparedStatement != null;
                            preparedStatement.setObject(paramIdx, obj);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    });
        }

        // execute sql
        ResultSet resultSet = Optional.ofNullable(preparedStatement)
                .map(preparedStatement1 -> {
                    try {
                        return preparedStatement1.executeQuery();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .orElse(null);
        // return resultSet
        String resultTypeStr = mapperStatement.getResultType();
        List<Object> results = Optional.ofNullable(resultSet)
                .map(rs -> {
                    ArrayList<Object> objs = new ArrayList<>();
                    try {
                        Class<?> resultTypeClass = getClassType(resultTypeStr);
                        while (rs.next()) {
                            // get meta data
                            ResultSetMetaData metaData = rs.getMetaData();
                            Object resultClassInstance = resultTypeClass.newInstance();
                            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                                String columnName = metaData.getColumnName(i);
                                Object objPerColumn = rs.getObject(columnName);
                                // 使用反射或者内省，根据数据库表和实体的对应关系，完成封装
                                PropertyDescriptor propertyDescriptor = new PropertyDescriptor(columnName, resultTypeClass);
                                Method writeMethod = propertyDescriptor.getWriteMethod();
                                writeMethod.invoke(resultClassInstance, objPerColumn);
                            }
                            objs.add(resultClassInstance);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return objs;
                }).get();
        return (List<E>) results;
    }

    /**
     * return class type by parameter type
     * @param typeStr String of parameter type
     * @return return specific class type
     */
    private Class<?> getClassType(String typeStr) {
        return Optional.ofNullable(typeStr)
                .map(type -> {
                    Class<?> aClass = null;
                    try {
                        aClass = Class.forName(type);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    return aClass;
                }).orElse(null);
    }

    /**
     * 1. analysis the placeholder #{}
     * 2. get the value in the placeholder
     *
     * @return return parsed sql
     */
    private BoundSql getBoundSql(String sql) {
        TokenHandler parameterMappingTokenHandler = new ParameterMappingTokenHandler();
        GenericTokenParser genericTokenParser = new GenericTokenParser("#{", "}", parameterMappingTokenHandler);
        // parsed sql
        String parsedSql = genericTokenParser.parse(sql);
        // parsed parameters names in #{}
        List<ParameterMapping> parameterMappings =
                ((ParameterMappingTokenHandler) parameterMappingTokenHandler).getParameterMappings();
        return new BoundSql(parsedSql, parameterMappings);
    }

}
