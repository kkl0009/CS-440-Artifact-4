package com.cs440.backend.backendapp3.rest;

//import static helpers.Configuration.JDBC_URL;

import java.sql.Connection;
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

import com.cs440.backend.backendapp3.database.ConnectionManager;
import com.cs440.backend.backendapp3.objects.KnownMove;
import com.cs440.backend.backendapp3.objects.Move;
import com.cs440.backend.backendapp3.objects.Pokemon;
import com.cs440.backend.backendapp3.objects.PokemonAndSpecies;
import com.cs440.backend.backendapp3.objects.Species;
import com.cs440.backend.backendapp3.objects.Trainer;

@RestController
public class DatabaseController {
	
	private static final Connection CONNECTION = ConnectionManager.getConnection();
	
	@GetMapping("/database")
	public String database() throws SQLException {
	
		final String query = "SELECT * FROM POKEMON";
		
		PreparedStatement prep = CONNECTION.prepareStatement(query);
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
		Statement stmt = CONNECTION.createStatement();
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
	
	public static List<Trainer> getTrainersInArea(int id) throws SQLException {
		String query = "SELECT * FROM TRAINER WHERE LOCATIONID = ?";
		PreparedStatement prep = CONNECTION.prepareStatement(query);
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
		PreparedStatement prep = CONNECTION.prepareStatement(query);
		prep.setInt(1, id);
		ResultSet rs = prep.executeQuery();
		if (rs.next()) {
			return new Trainer(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getInt(4));
		}
		else return null;
	}
	
	public static List<Pokemon> getTrainerPokemon(int id) throws SQLException {
		String query = "SELECT * FROM POKEMON WHERE TRAINERID = ?";
		PreparedStatement prep = CONNECTION.prepareStatement(query);
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
		PreparedStatement prep = CONNECTION.prepareStatement(query);
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
		PreparedStatement prep = CONNECTION.prepareStatement(query);
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
		PreparedStatement prep = CONNECTION.prepareStatement(query);
		prep.setInt(1, pokedexNum);
		ResultSet rs = prep.executeQuery();
		if (rs.next()) {
			return new Species(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(5));
		}
		else return null;
	}
	
	public static List<Move> getLearnableMoves(int pokedexNum) throws SQLException {
		String query = "SELECT MOVE.* FROM MOVE, CAN_LEARN WHERE CAN_LEARN.SPECIESNUM = ? AND CAN_LEARN.MOVEID = MOVE.ID";
		PreparedStatement prep = CONNECTION.prepareStatement(query);
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
		PreparedStatement prep = CONNECTION.prepareStatement(delete);
		prep.setInt(1, pokedexNum);
		prep.executeUpdate();
	}
	
	public static void updateSpecies(Species s) throws SQLException {
		String update = "UPDATE SPECIES SET NAME = ?, TYPE1 = ?, TYPE2 = ?, EVOLUTIONNUM = ? WHERE POKEDEXNUM = ?";
		PreparedStatement prep = CONNECTION.prepareStatement(update);
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
	
	public static void deleteTrainer(int id) throws SQLException {
		String delete = "DELETE FROM TRAINER WHERE ID = ?";
		PreparedStatement prep = CONNECTION.prepareStatement(delete);
		prep.setInt(1, id);
		prep.executeUpdate();
	}
	
	public static void updateTrainer(Trainer t) throws SQLException {
		String update = "UPDATE TRAINER SET NAME = ?, REWARDMONEY = ?, LOCATIONID = ? WHERE ID = ?";
		PreparedStatement prep = CONNECTION.prepareStatement(update);
		prep.setString(1, t.getName());
		prep.setInt(2, t.getRewardMoney());
		prep.setInt(3,  t.getLocationId());
		prep.setInt(4, t.getId());
		prep.executeUpdate();
	}
	
	public static int getNextPokedexNum() throws SQLException {
		String query = "SELECT MAX(POKEDEXNUM) FROM SPECIES";
		ResultSet rs = executeQuery(query);
		rs.next();
		return rs.getInt(1) + 1;
	}
	
	public static int getNextTrainerId() throws SQLException {
		String query = "SELECT MAX(ID) FROM TRAINER";
		ResultSet rs = executeQuery(query);
		rs.next();
		return rs.getInt(1) + 1;
	}
	
	public static int getNextPokemonId() throws SQLException {
		String query = "SELECT MAX(ID) FROM POKEMON";
		ResultSet rs = executeQuery(query);
		rs.next();
		return rs.getInt(1) + 1;
	}
	
	public static void addTrainer(Trainer t) throws SQLException {
		String insert = "INSERT INTO TRAINER VALUES (?, ?, ?, ?)";
		PreparedStatement prep = CONNECTION.prepareStatement(insert);
		prep.setInt(1, t.getId());
		prep.setString(2, t.getName());
		prep.setInt(3, t.getRewardMoney());
		prep.setInt(4, t.getLocationId());
		prep.executeUpdate();
	}
	
	public static void addPokemon(Pokemon p) throws SQLException {
		String insert = "INSERT INTO POKEMON VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement prep = CONNECTION.prepareStatement(insert);
		prep.setInt(1, p.getId());
		prep.setInt(2, p.getLevel());
		prep.setString(3, p.getNature());
		prep.setString(4, p.getAbility());
		prep.setInt(5, p.getHp());
		prep.setInt(6, p.getAtk());
		prep.setInt(7, p.getDef());
		prep.setInt(8, p.getSpd());
		prep.setInt(9, p.getSpc());
		prep.setInt(10, p.getTrainerId());
		prep.setInt(11, p.getSpeciesNum());
		prep.executeUpdate();
	}
	
	public static void addSpecies(Species s) throws SQLException {
		String insert = "INSERT INTO SPECIES VALUES (?, ?, ?, ?, ?)";
		PreparedStatement prep = CONNECTION.prepareStatement(insert);
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
	
	public static List<Move> getAllMoves() throws SQLException {
		String query = "SELECT * FROM MOVE ORDER BY ID";
		ResultSet rs = executeQuery(query);
		LinkedList<Move> moves = new LinkedList<Move>();
		while (rs.next()) {
			moves.add(new Move(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getInt(4), rs.getString(5), rs.getString(6)));
		}
		return moves;
	}
	
	public static Move getMove(int id) throws SQLException {
		String query = "SELECT * FROM MOVE WHERE ID = ?";
		PreparedStatement prep = CONNECTION.prepareStatement(query);
		prep.setInt(1, id);
		ResultSet rs = prep.executeQuery();
		if (rs.next()) {
			return new Move(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getInt(4), rs.getString(5), rs.getString(6));
		}
		else return null;
	}
	
	public static void updateMove(Move m) throws SQLException {
		String update = "UPDATE MOVE SET NAME = ?, POWER = ?, ACCURACY = ?, TYPE = ?, DESCRIPTION = ? WHERE ID = ?";
		PreparedStatement prep = CONNECTION.prepareStatement(update);
		prep.setString(1, m.getName());
		prep.setInt(2, m.getPower());
		prep.setInt(3, m.getAccuracy());
		prep.setString(4, m.getType());
		prep.setString(5, m.getDescription());
		prep.setInt(6, m.getId());
		prep.executeUpdate();
	}
	
	public static void deleteMove(int id) throws SQLException {
		String delete = "DELETE FROM MOVE WHERE ID = ?";
		PreparedStatement prep = CONNECTION.prepareStatement(delete);
		prep.setInt(1, id);
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
	
	public static Pokemon getPokemon(int id) throws SQLException {
		String query = "SELECT * FROM POKEMON WHERE ID = ?";
		PreparedStatement prep = CONNECTION.prepareStatement(query);
		prep.setInt(1, id);
		ResultSet rs = prep.executeQuery();
		if (rs.next()) {
			return new Pokemon(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4), rs.getInt(5), rs.getInt(6), rs.getInt(7), rs.getInt(8), rs.getInt(9),  rs.getInt(10), rs.getInt(11));
		}
		else return null;
	}
	
	public static List<Species> getLearnableSpecies(int id) throws SQLException {
		String query = "SELECT SPECIES.* FROM MOVE, CAN_LEARN, SPECIES WHERE MOVE.ID = ? AND CAN_LEARN.MOVEID = MOVE.ID AND CAN_LEARN.SPECIESNUM = SPECIES.POKEDEXNUM";
		PreparedStatement prep = CONNECTION.prepareStatement(query);
		prep.setInt(1, id);
		ResultSet rs = prep.executeQuery();
		LinkedList<Species> species = new LinkedList<Species>();
		while (rs.next()) {
			species.add(new Species(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(5)));
		}
		return species;
	}

	public static void updatePokemon(Pokemon p) throws SQLException {
		String update = "UPDATE POKEMON SET POKEMONLEVEL = ?, NATURE = ?, ABILITY = ?, HP = ?, ATK = ?, DEF = ?, SPD = ?, SPC = ?, SPECIESNUM = ? WHERE ID = ?";
		PreparedStatement prep = CONNECTION.prepareStatement(update);
		prep.setInt(1, p.getLevel());
		prep.setString(2, p.getNature());
		prep.setString(3, p.getAbility());
		prep.setInt(4, p.getHp());
		prep.setInt(5, p.getAtk());
		prep.setInt(6, p.getDef());
		prep.setInt(7, p.getSpd());
		prep.setInt(8, p.getSpc());
		prep.setInt(9, p.getSpeciesNum());
		prep.setInt(10, p.getId());
		prep.executeUpdate();
	}
	
	public static void deletePokemon(int id) throws SQLException {
		String delete = "DELETE FROM POKEMON WHERE ID = ?";
		PreparedStatement prep = CONNECTION.prepareStatement(delete);
		prep.setInt(1, id);
		prep.executeUpdate();
	}
	
	public static void addKnownMove(KnownMove km) throws SQLException {
		String insert = "INSERT INTO KNOWN_MOVES VALUES (?, ?)";
		PreparedStatement prep = CONNECTION.prepareStatement(insert);
		prep.setInt(1, km.getPokemonId());
		prep.setInt(2, km.getMoveId());
		prep.executeUpdate();
	}
	
	public static void deleteKnownMove(int pokemonId, int moveId) throws SQLException {
		String delete = "DELETE FROM KNOWN_MOVES WHERE POKEMONID = ? AND MOVEID = ?";
		PreparedStatement prep = CONNECTION.prepareStatement(delete);
		prep.setInt(1, pokemonId);
		prep.setInt(2, moveId);
		prep.executeUpdate();
	}
	
}