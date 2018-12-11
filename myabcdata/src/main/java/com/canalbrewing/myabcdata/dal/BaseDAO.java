package com.canalbrewing.myabcdata.dal;

import java.sql.Connection;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;

public class BaseDAO {
	
	@Autowired
	protected DataSource dataSource;
	
	protected Connection getConnection() throws Exception
	{
		return this.dataSource.getConnection();
	}
	
	protected void rollbackConnection(Connection conn)
	{
		if ( conn == null )
		{
			return;
		}
		
		try
		{
			conn.rollback();
		}
		catch ( Exception ex )
		{
			ex.printStackTrace();
		}
	}

}
