package com.peter8icestone.pojo;

import com.peter8icestone.enums.SqlCommandType;

public class MapperStatement {

    private String id;
    private String parameterType;
    private String resultType;
    private String sql;
    private SqlCommandType sqlCommandType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParameterType() {
        return parameterType;
    }

    public void setParameterType(String parameterType) {
        this.parameterType = parameterType;
    }

    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public SqlCommandType getSqlCommandType() {
        return sqlCommandType;
    }

    public void setSqlCommandType(SqlCommandType sqlCommandType) {
        this.sqlCommandType = sqlCommandType;
    }

    @Override
    public String toString() {
        return "MapperStatement{" +
                "id='" + id + '\'' +
                ", parameterType='" + parameterType + '\'' +
                ", resultType='" + resultType + '\'' +
                ", sql='" + sql + '\'' +
                ", sqlCommandType=" + sqlCommandType +
                '}';
    }
}
