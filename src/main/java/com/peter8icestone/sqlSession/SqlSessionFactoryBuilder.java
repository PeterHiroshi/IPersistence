package com.peter8icestone.sqlSession;

import com.peter8icestone.config.XMLConfigBuilder;
import com.peter8icestone.pojo.Configuration;
import org.dom4j.DocumentException;

import java.beans.PropertyVetoException;
import java.io.InputStream;

public class SqlSessionFactoryBuilder {

    public SqlSessionFactory build(InputStream in) throws DocumentException, PropertyVetoException {
        /*
         * use dom4j to analysis the configure file
         * and encapsulate to Configuration object
         */
        XMLConfigBuilder xmlConfigBuilder = new XMLConfigBuilder();
        Configuration configuration = xmlConfigBuilder.parseConfig(in);
        /*
         * create SqlSessionFactory object to produce SqlSession object (Factory Pattern)
         */
        SqlSessionFactory defaultSqlSessionFactory = new DefaultSqlSessionFactory(configuration);
        return defaultSqlSessionFactory;
    }
}
