package com.leyou.config;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

//@Configuration
public class DataSourceConfig {

   /* @Bean
    @Primary
    public DataSourceProperties get() {
        DataSourceProperties dataSourceProperties = new DataSourceProperties();
        ClassLoader classLoader = String.class.getClassLoader();
        String name = "mydb";
        boolean generateUniqueName = true;
        Class type = MysqlDataSource.class;
        String driverClassName = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/yun6";
        String username = "root";
        String password = "123456";
        dataSourceProperties.setBeanClassLoader(classLoader);
        dataSourceProperties.setName(name);
        dataSourceProperties.setGenerateUniqueName(generateUniqueName);
        dataSourceProperties.setType(type);
        dataSourceProperties.setDriverClassName(driverClassName);
        dataSourceProperties.setUrl(url);
        dataSourceProperties.setUsername(username);
        dataSourceProperties.setPassword(password);
        return dataSourceProperties;
    }*/

    /*@Bean
    public DataSource getMyDataSource() {
        return DataSourceBuilder.create()
                .username("root")
                .password("123456")
                .url("jdbc:mysql://localhost:3306/yun6")
                .driverClassName("com.mysql.jdbc.Driver")
                .build();
    }*/
}
