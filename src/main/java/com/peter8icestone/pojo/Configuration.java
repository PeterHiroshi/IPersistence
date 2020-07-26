package com.peter8icestone.pojo;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

public class Configuration {

    private DataSource dataSource;
    /**
     * key: statementId (namespace.id)
     * value: encapsulated MapperStatement object
     */
    private Map<String, MapperStatement> mappedStatementMap = new HashMap<>();

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Map<String, MapperStatement> getMappedStatementMap() {
        return mappedStatementMap;
    }

    public void setMappedStatementMap(Map<String, MapperStatement> mappedStatementMap) {
        this.mappedStatementMap = mappedStatementMap;
    }
}
