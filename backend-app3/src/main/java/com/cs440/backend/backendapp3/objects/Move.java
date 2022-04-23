package com.cs440.backend.backendapp3.objects;

public class Move {

	private int id;
	private String name;
	private int power;
	private int accuracy;
	private String type;
	private String description;
	
	public Move() {
		id = 0;
		name = null;
		power = 0;
		accuracy = 0;
		type = null;
		description = null;
	}
	
	public Move(int id, String name, int power, int accuracy, String type, String description) {
		this.id = id;
		this.name = name;
		this.power = power;
		this.accuracy = accuracy;
		this.type = type;
		this.description = description;
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
	
	public int getPower() {
		return power;
	}
	
	public void setPower(int power) {
		this.power = power;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}

	public int getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(int accuracy) {
		this.accuracy = accuracy;
	}
	
}
