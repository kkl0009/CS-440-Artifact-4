package com.cs440.backend.backendapp3.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import com.cs440.backend.backendapp3.objects.Area;
import com.cs440.backend.backendapp3.objects.SpeciesAndSpawnRate;

public class AreaManager {

	private static final Connection CONNECTION = ConnectionManager.getConnection();
	
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
	
}
