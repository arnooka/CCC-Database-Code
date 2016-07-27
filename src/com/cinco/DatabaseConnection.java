package com.cinco;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.*;

public class DatabaseConnection {
	
	private static org.apache.log4j.Logger log = Logger.getLogger(DatabaseConnection.class);
	
	private static final String url = "jdbc:mysql://cse.unl.edu/anooka";
	private static final String username = "anooka";
	private static final String password = "Zi:LB4";
	// Password for reference: Zi:LB4
	public static Connection getConnection() {
		// Establish Database Connection
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (InstantiationException e) {
			log.error("InstantiationException: ", e);
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			log.error("IllegalAccessException: ", e);
			throw new RuntimeException(e);
		} catch (ClassNotFoundException e) {
			log.error("ClassNotFoundException: ", e);
			throw new RuntimeException(e);
		}
		
		Connection conn = null;

		try {
			conn = DriverManager.getConnection(url, username, password);
		} catch (SQLException e) {
			log.error("SQLException: ", e);
			throw new RuntimeException(e);
		}
		return conn;
	}
}
