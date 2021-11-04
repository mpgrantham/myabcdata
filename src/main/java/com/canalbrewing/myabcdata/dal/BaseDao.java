package com.canalbrewing.myabcdata.dal;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class BaseDao {

    @Qualifier("dsMyabcdata")
    @Autowired
    protected DataSource dataSource;

    protected Connection getConnection() throws SQLException {
		  return this.dataSource.getConnection();
    }
 
    
}