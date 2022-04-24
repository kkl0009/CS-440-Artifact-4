package com.cs440.backend.backendapp3.objects;

public class Pokemon {

	private int id;
	private int level;
	private String nature;
	private String ability;
	private int hp;
	private int atk;
	private int def;
	private int spd;
	private int spc;
	private int trainerId;
	private int speciesNum;
	
	public Pokemon() {
		id = 0;
		level = 0;
		nature = null;
		ability = null;
		hp = 0;
		atk = 0;
		def = 0;
		spd = 0;
		spc = 0;
		trainerId = 0;
		speciesNum = 0;
	}
	
	public Pokemon(int id, int level, String nature, String ability, int hp, int atk, int def, int spd, int spc, int trainerId, int speciesNum) {
		this.id = id;
		this.level = level;
		this.nature = nature;
		this.ability = ability;
		this.hp = hp;
		this.atk = atk;
		this.def = def;
		this.spd = spd;
		this.spc = spc;
		this.trainerId = trainerId;
		this.speciesNum = speciesNum;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public String getNature() {
		return nature;
	}
	public void setNature(String nature) {
		this.nature = nature;
	}
	public String getAbility() {
		return ability;
	}
	public void setAbility(String ability) {
		this.ability = ability;
	}
	public int getHp() {
		return hp;
	}
	public void setHp(int hp) {
		this.hp = hp;
	}
	public int getAtk() {
		return atk;
	}
	public void setAtk(int atk) {
		this.atk = atk;
	}
	public int getDef() {
		return def;
	}
	public void setDef(int def) {
		this.def = def;
	}
	public int getSpd() {
		return spd;
	}
	public void setSpd(int spd) {
		this.spd = spd;
	}
	public int getSpc() {
		return spc;
	}
	public void setSpc(int spc) {
		this.spc = spc;
	}
	public int getTrainerId() {
		return trainerId;
	}
	public void setTrainerId(int trainerId) {
		this.trainerId = trainerId;
	}
	public int getSpeciesNum() {
		return speciesNum;
	}
	public void setSpeciesNum(int speciesNum) {
		this.speciesNum = speciesNum;
	}
	
}
