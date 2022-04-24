package com.cs440.backend.backendapp3.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {

	public static final String
		HOST = "lrp-csdb000.systems.wvu.edu",
		PORT = "2201",
		DATABASE = "cs440",
		JDBC_URL = String.format(
			"jdbc:oracle:thin:@//%s:%s/%s",
			HOST,
			PORT,
			DATABASE
	);
	
	private static Connection conn = null;
	
	public static Connection getConnection() {
		
		if (conn == null) {
			try {
				conn = DriverManager.getConnection(
						JDBC_URL,
						"TEAM_SIGMA",
						"634963"
					);
			}
			catch(SQLException e) {
				e.printStackTrace();
			}
		}
		return conn;
	}
	
}
