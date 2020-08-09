package com.peter8icestone.config;

import com.peter8icestone.enums.SqlCommandType;
import com.peter8icestone.pojo.Configuration;
import com.peter8icestone.pojo.MapperStatement;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class XMLMapperBuilder {

    private Configuration configuration;

    public XMLMapperBuilder(Configuration configuration) {
        this.configuration = configuration;
    }

    public void parse(InputStream in) throws DocumentException {
        // analysis mapper.xml using dom4j
        Document mapperDocument = new SAXReader().read(in);
        // get the element of <mapper> tag
        Element mapperElement = mapperDocument.getRootElement();
        String namespace = mapperElement.attributeValue("namespace");
        List<Element> subElements = mapperElement.selectNodes("insert|select|update|delete");
        subElements.forEach(element -> {
            String id = element.attributeValue("id");
            String parameterType = element.attributeValue("parameterType");
            String resultType = element.attributeValue("resultType");
            String sql = element.getTextTrim();
            SqlCommandType sqlCommandType = SqlCommandType.valueOf(element.getName().toUpperCase(Locale.ENGLISH));
            MapperStatement mapperStatement = new MapperStatement();
            mapperStatement.setId(id);
            mapperStatement.setParameterType(parameterType);
            mapperStatement.setResultType(resultType);
            mapperStatement.setSql(sql);
            mapperStatement.setSqlCommandType(sqlCommandType);
            String key = namespace + "." + id;
            configuration.getMappedStatementMap().put(key, mapperStatement);
        });
    }
}
