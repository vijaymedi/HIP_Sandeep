package com.iig.gcp.hip.hipcontroller.utils;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ConnectionUtils {

	@Autowired
	private DataSource dataSource;
    /**
     * 
     * @return Connection
     * @throws ClassNotFoundException
     * @throws SQLException
     * @throws Exception
     */
	public Connection getConnection() throws ClassNotFoundException, SQLException, Exception {
		// return OracleConnUtils.getOracleConnection();
		return dataSource.getConnection();
	}

}
