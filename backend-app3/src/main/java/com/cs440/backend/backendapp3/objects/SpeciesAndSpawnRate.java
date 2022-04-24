package com.cs440.backend.backendapp3.objects;

public class SpeciesAndSpawnRate {

	private Species species;
	private float spawnRate;
	
	public SpeciesAndSpawnRate() {
		species = new Species();
		spawnRate = 0;
	}
	
	public SpeciesAndSpawnRate(int pokedexNum, String name, String type1, String type2, int evolutionNum, float spawnRate) {
		this.species = new Species(pokedexNum, name, type1, type2, evolutionNum);
		this.spawnRate = spawnRate;
	}
	
	public Species getSpecies() {
		return species;
	}
	public void setSpecies(Species species) {
		this.species = species;
	}
	public float getSpawnRate() {
		return spawnRate;
	}
	public void setSpawnRate(float spawnRate) {
		this.spawnRate = spawnRate;
	}
	
}
