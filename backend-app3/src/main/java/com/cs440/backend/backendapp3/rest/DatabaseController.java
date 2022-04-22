package com.cs440.backend.backendapp3.rest;

//import static helpers.Configuration.JDBC_URL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cs440.backend.backendapp3.objects.Species;

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
	
	private static Connection con;
	
	static {
		try {
			con = DriverManager.getConnection(
					JDBC_URL,
					"TEAM_SIGMA",
					"634963"
				);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@GetMapping("/database")
	public String database() throws SQLException {
	
		final String query = "SELECT * FROM POKEMON";
		
		PreparedStatement prep = con.prepareStatement(query);
		ResultSet rs = prep.executeQuery();
		
		final ResultSetMetaData meta = rs.getMetaData();
		return meta.getColumnName(1);
		//Right now it just reads the first column name, but this proves that it is connecting to the database
		//TODO: Create a Table object to place all of the columns and information into, then use this object for sending tables to the frontend
	}
	
	@GetMapping("/getSpecies")
	public List<List<Object>> getSpecies() throws SQLException {
		final String query = "SELECT * FROM SPECIES";
		ResultSet rs = executeQuery(query);
		
		LinkedList<List<Object>> returnList = new LinkedList<List<Object>>();
		while (rs.next()) {
			returnList.add(Arrays.asList(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(5)));
		}
		return returnList;
		
	}
	
	public static ResultSet executeQuery(String query) throws SQLException {
		Statement stmt = con.createStatement();
		return stmt.executeQuery(query);
	}
	
	public static Species getSpecies(int pokedexNum) throws SQLException {
		String query = "SELECT * FROM SPECIES WHERE POKEDEXNUM = ?";
		PreparedStatement prep = con.prepareStatement(query);
		prep.setInt(1, pokedexNum);
		ResultSet rs = prep.executeQuery();
		if (rs.next()) {
			return new Species(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(5));
		}
		else return null;
	}
}