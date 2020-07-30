package com.peter8icestone.sqlSession;

import com.peter8icestone.config.BoundSql;
import com.peter8icestone.pojo.Configuration;
import com.peter8icestone.pojo.MapperStatement;
import com.peter8icestone.utils.GenericTokenParser;
import com.peter8icestone.utils.ParameterMapping;
import com.peter8icestone.utils.ParameterMappingTokenHandler;
import com.peter8icestone.utils.TokenHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
        //TODO set parameters
        //TODO execute sql
        //TODO return resultSet
        return null;
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
