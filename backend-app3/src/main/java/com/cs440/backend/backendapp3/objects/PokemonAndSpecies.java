package com.cs440.backend.backendapp3.objects;

public class PokemonAndSpecies {

	private Pokemon pokemon;
	private Species species;
	
	public PokemonAndSpecies(Pokemon pokemon, Species species) {
		this.pokemon = pokemon;
		this.species = species;
	}
	
	public Pokemon getPokemon() {
		return pokemon;
	}
	public void setPokemon(Pokemon pokemon) {
		this.pokemon = pokemon;
	}
	public Species getSpecies() {
		return species;
	}
	public void setSpecies(Species species) {
		this.species = species;
	}
	
}
