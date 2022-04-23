package com.cs440.backend.backendapp3.objects;

public class Trainer {

	private int id;
	private String name;
	private int rewardMoney;
	private int locationId;
	
	public Trainer() {
		id = 0;
		name = null;
		rewardMoney = 0;
		locationId = 0;
	}
	
	public Trainer(int id, String name, int rewardMoney, int locationId) {
		this.id = id;
		this.name = name;
		this.rewardMoney = rewardMoney;
		this.locationId = locationId;
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
	public int getRewardMoney() {
		return rewardMoney;
	}
	public void setRewardMoney(int rewardMoney) {
		this.rewardMoney = rewardMoney;
	}
	public int getLocationId() {
		return locationId;
	}
	public void setLocationId(int locationId) {
		this.locationId = locationId;
	}
	
}
