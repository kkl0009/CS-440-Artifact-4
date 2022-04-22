package com.cs440.backend.backendapp3.objects;

public class Species {

	private int pokedexNum;
	private String name;
	private String type1;
	private String type2;
	private int evolutionNum;
	
	public Species(int pokedexNum, String name, String type1, String type2, int evolutionNum) {
		this.pokedexNum = pokedexNum;
		this.name = name;
		this.type1 = type1;
		this.type2 = type2;
		this.evolutionNum = evolutionNum;
	}
	
	public int getPokedexNum() {
		return pokedexNum;
	}
	
	public void setPokedexNum(int pokedexNum) {
		this.pokedexNum = pokedexNum;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getType1() {
		return type1;
	}
	
	public void setType1(String type1) {
		this.type1 = type1;
	}
	
	public String getType2() {
		return type2;
	}
	
	public void setType2(String type2) {
		this.type2 = type2;
	}
	
	public int getEvolutionNum() {
		return evolutionNum;
	}
	
	public void setEvolutionNum(int evolutionNum) {
		this.evolutionNum = evolutionNum;
	}
	
}
