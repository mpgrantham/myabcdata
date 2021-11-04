package com.canalbrewing.myabcdata.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class DatabaseConfig {

    @Bean(name = "dsMyabcdata")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource createDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "jdbcMyabcdata")
    @Autowired
    public JdbcTemplate createJdbcTemplate(@Qualifier("dsMyabcdata") DataSource myabcdataDS) {
        return new JdbcTemplate(myabcdataDS);
    }

    
}