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

import com.cs440.backend.backendapp3.objects.Area;
import com.cs440.backend.backendapp3.objects.Move;
import com.cs440.backend.backendapp3.objects.Pokemon;
import com.cs440.backend.backendapp3.objects.PokemonAndSpecies;
import com.cs440.backend.backendapp3.objects.Species;
import com.cs440.backend.backendapp3.objects.SpeciesAndSpawnRate;
import com.cs440.backend.backendapp3.objects.Trainer;

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
		final String query = "SELECT * FROM SPECIES ORDER BY POKEDEXNUM";
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
		final String query = "SELECT * FROM SPECIES ORDER BY POKEDEXNUM";
		ResultSet rs = executeQuery(query);
		
		LinkedList<Species> returnList = new LinkedList<Species>();
		while (rs.next()) {
			returnList.add(new Species(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(5)));
		}
		return returnList;
	}
	
	public static List<Area> getAllAreas() throws SQLException {
		String query = "SELECT * FROM AREA ORDER BY ID";
		ResultSet rs = executeQuery(query);
		LinkedList<Area> areas = new LinkedList<Area>();
		while (rs.next()) {
			areas.add(new Area(rs.getInt(1), rs.getString(2), rs.getString(3)));
		}
		return areas;
	}
	
	public static Area getArea(int id) throws SQLException {
		String query = "SELECT * FROM AREA WHERE ID = ?";
		PreparedStatement prep = con.prepareStatement(query);
		prep.setInt(1, id);
		ResultSet rs = prep.executeQuery();
		if (rs.next()) {
			return new Area(rs.getInt(1), rs.getString(2), rs.getString(3));
		}
		else return null;
	}
	
	public static List<Area> getAdjacentAreas(int id) throws SQLException {
		String query = "SELECT AREA.* FROM AREA, ADJACENT_TO WHERE ADJACENT_TO.AREA1ID = ? AND ADJACENT_TO.AREA2ID = AREA.ID";
		PreparedStatement prep = con.prepareStatement(query);
		prep.setInt(1, id);
		ResultSet rs = prep.executeQuery();
		LinkedList<Area> areas = new LinkedList<Area>();
		while (rs.next()) {
			areas.add(new Area(rs.getInt(1), rs.getString(2), rs.getString(3)));
		}
		return areas;
	}
	
	public static List<String> getLandmarks(int id) throws SQLException {
		String query = "SELECT LANDMARK FROM LANDMARKS WHERE LANDMARKS.AREAID = ?";
		PreparedStatement prep = con.prepareStatement(query);
		prep.setInt(1, id);
		ResultSet rs = prep.executeQuery();
		LinkedList<String> landmarks = new LinkedList<String>();
		while (rs.next()) {
			landmarks.add(rs.getString(1));
		}
		return landmarks;
	}
	
	public static List<SpeciesAndSpawnRate> getSpawns(int id) throws SQLException {
		String query = "SELECT SPECIES.*, SPAWNS_IN.SpawnRate FROM SPECIES, SPAWNS_IN WHERE SPAWNS_IN.AreaID = ? AND SPAWNS_IN.SpeciesNum = SPECIES.PokedexNum";
		PreparedStatement prep = con.prepareStatement(query);
		prep.setInt(1, id);
		ResultSet rs = prep.executeQuery();
		LinkedList<SpeciesAndSpawnRate> spawns = new LinkedList<SpeciesAndSpawnRate>();
		while (rs.next()) {
			spawns.add(new SpeciesAndSpawnRate(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(5), rs.getInt(6)));
		}
		return spawns;
	}
	
	public static List<Trainer> getTrainersInArea(int id) throws SQLException {
		String query = "SELECT * FROM TRAINER WHERE LOCATIONID = ?";
		PreparedStatement prep = con.prepareStatement(query);
		prep.setInt(1, id);
		ResultSet rs = prep.executeQuery();
		LinkedList<Trainer> trainers = new LinkedList<Trainer>();
		while (rs.next()) {
			trainers.add(new Trainer(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getInt(4)));
		}
		return trainers;
	}
	
	public static Trainer getTrainer(int id) throws SQLException {
		String query = "SELECT * FROM TRAINER WHERE ID = ?";
		PreparedStatement prep = con.prepareStatement(query);
		prep.setInt(1, id);
		ResultSet rs = prep.executeQuery();
		if (rs.next()) {
			return new Trainer(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getInt(4));
		}
		else return null;
	}
	
	public static List<Pokemon> getTrainerPokemon(int id) throws SQLException {
		String query = "SELECT * FROM POKEMON WHERE TRAINERID = ?";
		PreparedStatement prep = con.prepareStatement(query);
		prep.setInt(1, id);
		ResultSet rs = prep.executeQuery();
		LinkedList<Pokemon> p = new LinkedList<Pokemon>();
		while (rs.next()) {
			p.add(new Pokemon(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4), rs.getInt(5), rs.getInt(6), rs.getInt(7), rs.getInt(8), rs.getInt(9),  rs.getInt(10), rs.getInt(11)));
		}
		return p;
	}
	
	public static List<PokemonAndSpecies> getTrainerPokemonAndSpecies(int id) throws SQLException {
		String query = "SELECT POKEMON.*, SPECIES.* FROM POKEMON, SPECIES WHERE TRAINERID = ? AND SPECIESNUM = POKEDEXNUM";
		PreparedStatement prep = con.prepareStatement(query);
		prep.setInt(1, id);
		ResultSet rs = prep.executeQuery();
		LinkedList<PokemonAndSpecies> ps = new LinkedList<PokemonAndSpecies>();
		while (rs.next()) {
			Pokemon p = new Pokemon(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4), rs.getInt(5), rs.getInt(6), rs.getInt(7), rs.getInt(8), rs.getInt(9),  rs.getInt(10), rs.getInt(11));
			Species s  = new Species(rs.getInt(12), rs.getString(13), rs.getString(14), rs.getString(15), rs.getInt(16));
			ps.add(new PokemonAndSpecies(p, s));
		}
		return ps;
	}
	
	public static List<Move> getKnownMoves(int id) throws SQLException {
		String query = "SELECT MOVE.* FROM MOVE, KNOWN_MOVES WHERE KNOWN_MOVES.POKEMONID = ? AND KNOWN_MOVES.MOVEID = MOVE.ID";
		PreparedStatement prep = con.prepareStatement(query);
		prep.setInt(1, id);
		ResultSet rs = prep.executeQuery();
		LinkedList<Move> moves = new LinkedList<Move>();
		while (rs.next()) {
			moves.add(new Move(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getInt(4), rs.getString(5), rs.getString(6)));
		}
		return moves;
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
		String query = "SELECT MOVE.* FROM MOVE, CAN_LEARN WHERE CAN_LEARN.SPECIESNUM = ? AND CAN_LEARN.MOVEID = MOVE.ID";
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
	
	public static List<Trainer> getAllTrainers() throws SQLException {
		String query = "SELECT * FROM TRAINER ORDER BY ID";
		ResultSet rs = executeQuery(query);
		List<Trainer> t = new LinkedList<Trainer>();
		while (rs.next()) {
			t.add(new Trainer(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getInt(4)));
		}
		return t;
	}
	
}