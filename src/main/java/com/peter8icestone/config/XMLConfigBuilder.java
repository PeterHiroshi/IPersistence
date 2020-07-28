package com.peter8icestone.config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.peter8icestone.io.Resources;
import com.peter8icestone.pojo.Configuration;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.beans.PropertyVetoException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

public class XMLConfigBuilder {

    Configuration configuration;

    public XMLConfigBuilder() {
        configuration = new Configuration();
    }

    public Configuration parseConfig(InputStream in) throws DocumentException, PropertyVetoException {
        // use dom4j to analysis the database config
        Document document = new SAXReader().read(in);
        // get the element of <configuration> tag
        Element rootElement = document.getRootElement();
        List<Element> propertyElements = rootElement.selectNodes("//property");
        Properties properties = new Properties();
        propertyElements.forEach(element -> {
            properties.setProperty(element.attributeValue("name"),
                    element.attributeValue("value"));
        });
        // use c3p0 pool
        ComboPooledDataSource comboPooledDataSource = new ComboPooledDataSource();
        comboPooledDataSource.setDriverClass(properties.getProperty("driverClass"));
        comboPooledDataSource.setJdbcUrl(properties.getProperty("url"));
        comboPooledDataSource.setUser(properties.getProperty("username"));
        comboPooledDataSource.setPassword(properties.getProperty("password"));
        // encapsulate to Configuration object
        configuration.setDataSource(comboPooledDataSource);
        // analysis mapper.xml
        // step 1: get path of mapper.xml
        // step 2: convert to inputStream
        // step 3: analysis mapper.xml using dom4j
        List<Element> mapperElements = rootElement.selectNodes("//mapper");
        mapperElements.forEach(element -> {
            XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(configuration);
            String mapperPath = element.attributeValue("resource");
            try {
                xmlMapperBuilder.parse(Resources.getResourceAsStream(mapperPath));
            } catch (DocumentException e) {
                e.printStackTrace();
            }
        });
        return configuration;
    }
}
