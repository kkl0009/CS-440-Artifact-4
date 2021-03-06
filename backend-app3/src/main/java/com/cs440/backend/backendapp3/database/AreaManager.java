package com.cs440.backend.backendapp3.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.cs440.backend.backendapp3.objects.Area;
import com.cs440.backend.backendapp3.objects.Species;
import com.cs440.backend.backendapp3.objects.SpeciesAndSpawnRate;

public class AreaManager {

	private static final Connection CONNECTION = ConnectionManager.getConnection();
	
	public static int getNextAreaId() throws SQLException {
		String query = "SELECT MAX(ID) FROM TRAINER";
		ResultSet rs = CONNECTION.createStatement().executeQuery(query);
		rs.next();
		return rs.getInt(1) + 1;
	}
	
	public static void addArea(Area a) throws SQLException {
		String update = "INSERT INTO AREA VALUES (?, ?, ?)";
		PreparedStatement prep = CONNECTION.prepareStatement(update);
		prep.setInt(1, a.getId());
		prep.setString(2, a.getName());
		prep.setString(3, a.getTerrain());
		prep.executeUpdate();
	}
	
	public static void deleteArea(int id) throws SQLException {
		String delete = "DELETE FROM AREA WHERE ID = ?";
		PreparedStatement prep = CONNECTION.prepareStatement(delete);
		prep.setInt(1, id);
		prep.executeUpdate();
	}
	
	public static List<Area> getAllAreas() throws SQLException {
		String query = "SELECT * FROM AREA ORDER BY ID";
		ResultSet rs = CONNECTION.createStatement().executeQuery(query);
		LinkedList<Area> areas = new LinkedList<Area>();
		while (rs.next()) {
			areas.add(new Area(rs.getInt(1), rs.getString(2), rs.getString(3)));
		}
		return areas;
	}
	
	public static Area getArea(int id) throws SQLException {
		String query = "SELECT * FROM AREA WHERE ID = ?";
		PreparedStatement prep = CONNECTION.prepareStatement(query);
		prep.setInt(1, id);
		ResultSet rs = prep.executeQuery();
		if (rs.next()) {
			return new Area(rs.getInt(1), rs.getString(2), rs.getString(3));
		}
		else return null;
	}
	
	public static List<Area> getAdjacentAreas(int id) throws SQLException {
		String query = "SELECT AREA.* FROM AREA, ADJACENT_TO WHERE ADJACENT_TO.AREA1ID = ? AND ADJACENT_TO.AREA2ID = AREA.ID";
		PreparedStatement prep = CONNECTION.prepareStatement(query);
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
		PreparedStatement prep = CONNECTION.prepareStatement(query);
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
		PreparedStatement prep = CONNECTION.prepareStatement(query);
		prep.setInt(1, id);
		ResultSet rs = prep.executeQuery();
		LinkedList<SpeciesAndSpawnRate> spawns = new LinkedList<SpeciesAndSpawnRate>();
		while (rs.next()) {
			spawns.add(new SpeciesAndSpawnRate(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(5), rs.getInt(6)));
		}
		return spawns;
	}
	
	public static void updateArea(Area a) throws SQLException {
		String update = "UPDATE AREA SET NAME = ?, AREATYPE = ? WHERE ID = ?";
		PreparedStatement prep = CONNECTION.prepareStatement(update);
		prep.setString(1, a.getName());
		prep.setString(2, a.getTerrain());
		prep.setInt(3, a.getId());
		prep.executeUpdate();
	}
	
	public static void addLandmark(int areaId, String landmarkName) throws SQLException {
		String insert = "INSERT INTO LANDMARKS VALUES (?, ?)";
		PreparedStatement prep = CONNECTION.prepareStatement(insert);
		prep.setInt(1, areaId);
		prep.setString(2, landmarkName);
		prep.executeUpdate();
	}
	
	public static void deleteLandmark(int areaId, String landmarkName) throws SQLException {
		String delete = "DELETE FROM LANDMARKS WHERE AREAID = ? AND LANDMARK = ?";
		PreparedStatement prep = CONNECTION.prepareStatement(delete);
		prep.setInt(1, areaId);
		prep.setString(2, landmarkName);
		prep.executeUpdate();
	}
	
	public static List<SpeciesAndSpawnRate> getSpeciesAndSpawnRateInArea(int areaId) throws SQLException {
		String get = "SELECT * FROM SPAWNS_IN JOIN SPECIES ON SPECIESNUM = POKEDEXNUM WHERE AREAID = ?";
		PreparedStatement prep = CONNECTION.prepareStatement(get);
		prep.setInt(1, areaId);
		
		List<SpeciesAndSpawnRate> result = new LinkedList<>();
		ResultSet rs = prep.executeQuery();
		while (rs.next()) {
			SpeciesAndSpawnRate combined = new SpeciesAndSpawnRate(
					rs.getInt(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getInt(8), 
					rs.getFloat(3));
			result.add(combined);
		}
		
		return result;
	}
	
	public static void deleteSpeciesAndSpawnRate(int areaId, int pokedexNum) throws SQLException {
		String delete = "DELETE FROM SPAWNS_IN WHERE AREAID = ? AND SPECIESNUM = ?";
		PreparedStatement prep = CONNECTION.prepareStatement(delete);
		prep.setInt(1, areaId);
		prep.setInt(2, pokedexNum);
		prep.executeUpdate();
		return;
	}
	
	public static void addSpeciesAndSpawnRate(int areaId, int pokedexNum, float spawnRate) throws SQLException {
		String insert = "INSERT INTO SPAWNS_IN VALUES (?, ?, ?)";
		PreparedStatement prep = CONNECTION.prepareStatement(insert);
		prep.setInt(1, pokedexNum);
		prep.setInt(2, areaId);
		prep.setFloat(3, spawnRate);
		prep.executeUpdate();
	}
	
	public static void addAdjacency(int area1Id, int area2Id) throws SQLException {
		String insert = "INSERT INTO ADJACENT_TO VALUES (?, ?)";
		PreparedStatement prep = CONNECTION.prepareStatement(insert);
		prep.setInt(1, area1Id);
		prep.setInt(2, area2Id);
		prep.executeUpdate();
		
		PreparedStatement prep2 = CONNECTION.prepareStatement(insert);
		prep2.setInt(1, area2Id);
		prep2.setInt(2, area1Id);
		prep2.executeUpdate();
	}
	
	public static void deleteAdjacency(int area1Id, int area2Id) throws SQLException {
		String insert = "DELETE FROM ADJACENT_TO WHERE AREA1ID = ? AND AREA2ID = ?";
		PreparedStatement prep = CONNECTION.prepareStatement(insert);
		prep.setInt(1, area1Id);
		prep.setInt(2, area2Id);
		prep.executeUpdate();
		
		PreparedStatement prep2 = CONNECTION.prepareStatement(insert);
		prep2.setInt(1, area2Id);
		prep2.setInt(2, area1Id);
		prep2.executeUpdate();
	}
	
	public static List<Area> getNotAdjacentTo(int id) throws SQLException {
		String query = 
			"SELECT * \r\n"
				+ "FROM AREA\r\n"
				+ "WHERE ID != ?\r\n"
				+ "MINUS\r\n"
				+ "SELECT ID, NAME, AREATYPE\r\n"
				+ "FROM AREA \r\n"
				+ "    JOIN ADJACENT_TO ON ID = AREA1ID\r\n"
				+ "WHERE AREA2ID = ?";
		
		PreparedStatement prep = CONNECTION.prepareStatement(query);
		prep.setInt(1, id);
		prep.setInt(2, id);
		ResultSet rs = prep.executeQuery();
		LinkedList<Area> areas = new LinkedList<Area>();
		while (rs.next()) {
			areas.add(new Area(rs.getInt(1), rs.getString(2), rs.getString(3)));
		}
		return areas;
	}
	
	public static int getTotalRewardMoney(int id) throws SQLException {
		String query = 
			"SELECT SUM(TRAINER.RewardMoney)\r\n"
				+ "FROM TRAINER, AREA\r\n"
				+ "WHERE TRAINER.LocationID = AREA.ID \r\n"
				+ "    AND AREA.Id = ?";
				
		PreparedStatement prep = CONNECTION.prepareStatement(query);
		prep.setInt(1, id);
		ResultSet rs = prep.executeQuery();
		rs.next();
		return rs.getInt(1);
	}
	
	public static int getAveragePokemonLevel(int id) throws SQLException {
		String query = 
			"SELECT AVG(POKEMONLEVEL)\r\n"
				+ "FROM POKEMON, TRAINER, AREA\r\n"
				+ "WHERE TRAINER.LocationID = AREA.ID\r\n"
				+ "    AND POKEMON.TrainerID = TRAINER.ID \r\n"
				+ "    AND AREA.Id = ?";
		
		PreparedStatement prep = CONNECTION.prepareStatement(query);
		prep.setInt(1, id);
		ResultSet rs = prep.executeQuery();
		rs.next();
		return rs.getInt(1);
	}
	
}
