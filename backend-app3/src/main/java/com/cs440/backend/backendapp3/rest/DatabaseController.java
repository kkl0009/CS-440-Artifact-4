package com.cs440.backend.backendapp3.rest;

//import static helpers.Configuration.JDBC_URL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DatabaseController {

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
	
	@GetMapping("/database")
	public String database() throws SQLException {
		try (
				Connection con = DriverManager.getConnection(
					JDBC_URL,
					"TEAM_SIGMA",
					"634963"
				);
			) {
			final String query = "SELECT * FROM POKEMON";
			
			PreparedStatement prep = con.prepareStatement(query);
			ResultSet rs = prep.executeQuery();
			
			final ResultSetMetaData meta = rs.getMetaData();
			return meta.getColumnName(1);
			//Right now it just reads the first column name, but this proves that it is connecting to the database
			//TODO: Create a Table object to place all of the columns and information into, then use this object for sending tables to the frontend
		}
	}
}