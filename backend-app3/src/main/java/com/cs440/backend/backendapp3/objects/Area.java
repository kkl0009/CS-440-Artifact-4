package com.cs440.backend.backendapp3.objects;

public class Area {
	
	private int id;
	private String name;
	private String terrain;
	
	public Area() {
		id = 0;
		name = null;
		terrain = null;
	}
	
	public Area(int id, String name, String terrain) {
		this.id = id;
		this.name = name;
		this.terrain = terrain;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTerrain() {
		return terrain;
	}
	public void setTerrain(String terrain) {
		this.terrain = terrain;
	}
	
}
