package com.cs440.backend.backendapp3.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.cs440.backend.backendapp3.objects.Species;

public class SpeciesManager {

	private static final Connection CONNECTION = ConnectionManager.getConnection();
	
	public static Species getSpeciesByName(String name) throws SQLException {
		String query = "SELECT * FROM SPECIES WHERE UPPER(NAME) = ?";
		PreparedStatement prep = CONNECTION.prepareStatement(query);
		prep.setString(1, (name == null ? "" : name.toUpperCase()));
		ResultSet rs = prep.executeQuery();
		if (rs.next()) {
			return new Species(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(5));
		}
		else return null;
	}
	
}
