package com.cs440.backend.backendapp3.rest;

//import static helpers.Configuration.JDBC_URL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cs440.backend.backendapp3.objects.Move;
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
	
	public static List<Species> getAllSpecies() throws SQLException {
		final String query = "SELECT * FROM SPECIES";
		ResultSet rs = executeQuery(query);
		
		LinkedList<Species> returnList = new LinkedList<Species>();
		while (rs.next()) {
			returnList.add(new Species(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(5)));
		}
		return returnList;
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
	
	public static List<Move> getLearnableMoves(int pokedexNum) throws SQLException {
		String query = "SELECT MOVE.* FROM MOVE, CAN_LEARN, SPECIES WHERE SPECIES.POKEDEXNUM = ? AND CAN_LEARN.SPECIESNUM = SPECIES.POKEDEXNUM AND CAN_LEARN.MOVEID = MOVE.ID";
		PreparedStatement prep = con.prepareStatement(query);
		prep.setInt(1, pokedexNum);
		ResultSet rs = prep.executeQuery();
		LinkedList<Move> moves = new LinkedList<Move>();
		while (rs.next()) {
			moves.add(new Move(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getInt(4), rs.getString(5), rs.getString(6)));
		}
		return moves;
	}
	
	public static void deleteSpecies(int pokedexNum) throws SQLException {
		String delete = "DELETE FROM SPECIES WHERE POKEDEXNUM = ?";
		PreparedStatement prep = con.prepareStatement(delete);
		prep.setInt(1, pokedexNum);
		prep.executeUpdate();
	}
	
	public static void updateSpecies(Species s) throws SQLException {
		String update = "UPDATE SPECIES SET NAME = ?, TYPE1 = ?, TYPE2 = ?, EVOLUTIONNUM = ? WHERE POKEDEXNUM = ?";
		PreparedStatement prep = con.prepareStatement(update);
		prep.setString(1, s.getName());
		prep.setString(2, s.getType1());
		if (s.getType2() == null || s.getType2() == "") {
			prep.setNull(3, Types.VARCHAR);
		}
		else {
			prep.setString(3, s.getType2());
		}
		if (s.getEvolutionNum() == 0) {
			prep.setNull(4, Types.INTEGER);
		}
		else {
			prep.setInt(4, s.getEvolutionNum());
		}
		prep.setInt(5, s.getPokedexNum());
		prep.executeUpdate();
	}
	
	public static int getNextPokedexNum() throws SQLException {
		String query = "SELECT MAX(POKEDEXNUM) FROM SPECIES";
		ResultSet rs = executeQuery(query);
		rs.next();
		return rs.getInt(1) + 1;
	}
	
	public static void addSpecies(Species s) throws SQLException {
		String insert = "INSERT INTO SPECIES VALUES (?, ?, ?, ?, ?)";
		PreparedStatement prep = con.prepareStatement(insert);
		prep.setInt(1, s.getPokedexNum());
		prep.setString(2, s.getName());
		prep.setString(3, s.getType1());
		if (s.getType2() == null || s.getType2() == "") {
			prep.setNull(4, Types.VARCHAR);
		}
		else {
			prep.setString(4, s.getType2());
		}
		if (s.getEvolutionNum() == 0) {
			prep.setNull(5, Types.INTEGER);
		}
		else {
			prep.setInt(5, s.getEvolutionNum());
		}
		prep.executeUpdate();
	}
	
}