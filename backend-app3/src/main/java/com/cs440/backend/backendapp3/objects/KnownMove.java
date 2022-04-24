package com.cs440.backend.backendapp3.objects;

public class KnownMove {

	private int pokemonId;
	private int moveId;
	
	public KnownMove() {
		pokemonId = 0;
		moveId = 0;
	}
	
	public KnownMove(int pId, int mId) {
		pokemonId = pId;
		moveId = mId;
	}
	
	public int getPokemonId() {
		return pokemonId;
	}
	public void setPokemonId(int pokemonId) {
		this.pokemonId = pokemonId;
	}
	public int getMoveId() {
		return moveId;
	}
	public void setMoveId(int moveId) {
		this.moveId = moveId;
	}
	
}
