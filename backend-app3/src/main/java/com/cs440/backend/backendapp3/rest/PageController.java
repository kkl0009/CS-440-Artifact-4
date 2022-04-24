package com.cs440.backend.backendapp3.rest;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.cs440.backend.backendapp3.database.AreaManager;
import com.cs440.backend.backendapp3.objects.Area;
import com.cs440.backend.backendapp3.objects.KnownMove;
import com.cs440.backend.backendapp3.objects.Move;
import com.cs440.backend.backendapp3.objects.Pokemon;
import com.cs440.backend.backendapp3.objects.PokemonAndSpecies;
import com.cs440.backend.backendapp3.objects.Species;
import com.cs440.backend.backendapp3.objects.SpeciesAndSpawnRate;
import com.cs440.backend.backendapp3.objects.Trainer;

@Controller
public class PageController {

	@GetMapping("/areas")
	public ModelAndView areas() throws SQLException {
		ModelAndView mv = new ModelAndView("areas");
		List<Area> areas = AreaManager.getAllAreas();
		
		mv.addObject("areas", areas);
		return mv;
	}
	
	@GetMapping("/trainers")
	public ModelAndView trainers() throws SQLException {
		ModelAndView mv = new ModelAndView("trainers");
		List<Trainer> trainers = DatabaseController.getAllTrainers();
		
		mv.addObject("trainers", trainers);
		return mv;
	}
	
	@GetMapping("/pokemon")
	public String pokemon() {
		return "pokemon";
	}
	
	/**
	 * AREAS
	 */
	@GetMapping("/viewArea")
	public ModelAndView viewArea(@RequestParam(value = "id", required = true) int id) throws SQLException {
		ModelAndView mv = new ModelAndView("viewArea");
		Area area = AreaManager.getArea(id);
		List<Area> adjacentAreas = AreaManager.getAdjacentAreas(id);
		List<String> landmarks = AreaManager.getLandmarks(id);
		List<SpeciesAndSpawnRate> spawns = AreaManager.getSpawns(id);
		List<Trainer> trainers = DatabaseController.getTrainersInArea(id);
		
		mv.addObject("area", area);
		mv.addObject("adjacentAreas", adjacentAreas);
		mv.addObject("landmarks", landmarks);
		mv.addObject("spawns", spawns);
		mv.addObject("trainers", trainers);
		return mv;
	}
	
	@GetMapping("/deleteArea")
	public ModelAndView deleteArea(@RequestParam(value = "id", required = true) int id) throws SQLException {
		AreaManager.deleteArea(id);
		return areas();
	}
	
	@GetMapping("/editArea")
	public ModelAndView editArea(@RequestParam(value = "id", required = true) int id, Model model) throws SQLException {
		ModelAndView mv = new ModelAndView("editArea");
		Area area = AreaManager.getArea(id);
		
		mv.addObject("area", area);
		return mv;
	}
	
	@PostMapping("/updateArea")
	public ModelAndView updateArea(@ModelAttribute Area area, Model model) throws SQLException {
		AreaManager.updateArea(area);
		return viewArea(area.getId());
	}
	
	@GetMapping("/editLandmarks")
	public ModelAndView editLandmarks(@RequestParam(value = "id", required = true) int id, Model model) throws SQLException {
		ModelAndView mv = new ModelAndView("editLandmarks");
		Area area = AreaManager.getArea(id);
		List<String> landmarks = AreaManager.getLandmarks(id);
		
		mv.addObject("area", area);
		mv.addObject("landmarks", landmarks);
		return mv;
	}
	
	@PostMapping("/deleteLandmark")
	public ModelAndView deleteLandmark(int id, String landmarkName, Model model) throws SQLException {
		AreaManager.deleteLandmark(id, landmarkName);
		return editLandmarks(id, model);
	}
	
	@PostMapping("/addLandmark")
	public ModelAndView addLandmark(int id, String landmarkName, Model model) throws SQLException {
		AreaManager.addLandmark(id, landmarkName);
		return editLandmarks(id, model);
	}
	
	@GetMapping("/editSpeciesAndSpawnRates")
	public ModelAndView editSpeciesAndSpawnRates(@RequestParam(value = "id", required = true) int id, Model model) throws SQLException {
		ModelAndView mv = new ModelAndView("editSpeciesAndSpawnRates");
		Area area = AreaManager.getArea(id);
		List<SpeciesAndSpawnRate> result = AreaManager.getSpeciesAndSpawnRateInArea(id);
		
		mv.addObject("area", area);
		mv.addObject("SpeciesAndSpawnRateList", result);
		return mv;
	}
	
	@PostMapping("/deleteSpeciesAndSpawnRate")
	public ModelAndView deleteSpeciesAndSpawnRate(int areaId, int pokedexNum, Model model) throws SQLException {
		AreaManager.deleteSpeciesAndSpawnRate(areaId, pokedexNum);
		return editSpeciesAndSpawnRates(areaId, model);
	}

	/**
	 * TRAINERS
	 */
	@GetMapping("/viewTrainer")
	public ModelAndView viewTrainer(@RequestParam(value = "id", required = true) int id) throws SQLException {
		ModelAndView mv = new ModelAndView("viewTrainer");
		Trainer trainer = DatabaseController.getTrainer(id);
		List<PokemonAndSpecies> pokemonAndSpecies = DatabaseController.getTrainerPokemonAndSpecies(id);
		List<List<Move>> moves = new LinkedList<List<Move>>();
		for (PokemonAndSpecies p : pokemonAndSpecies) {
			moves.add(DatabaseController.getKnownMoves(p.getPokemon().getId()));
		}
		
		mv.addObject("trainer", trainer);
		mv.addObject("pokemonAndSpecies", pokemonAndSpecies);
		mv.addObject("moves", moves);
		return mv;
	}
	
	@GetMapping("/deleteTrainer")
	public ModelAndView deleteTrainer(@RequestParam(value = "id", required = true) int id) throws SQLException {
		DatabaseController.deleteTrainer(id);
		return trainers();
	}
	
	@GetMapping("/editTrainer")
	public ModelAndView editTrainer(@RequestParam(value = "id", required = true) int id, Model model) throws SQLException {
		ModelAndView mv = new ModelAndView("editTrainer");
		Trainer trainer = DatabaseController.getTrainer(id);
		List<Area> areas = AreaManager.getAllAreas();
		
		mv.addObject("trainer", trainer);
		mv.addObject("areas", areas);
		return mv;
	}
	
	@PostMapping("/editTrainer")
	public ModelAndView updateTrainer(@ModelAttribute Trainer trainer, Model model) throws SQLException {
		DatabaseController.updateTrainer(trainer);
		return viewTrainer(trainer.getId());
	}
	
	@GetMapping("/addTrainer")
	public ModelAndView addTrainer() throws SQLException {
		ModelAndView mv = new ModelAndView("addTrainer");
		int id = DatabaseController.getNextTrainerId();
		List<Area> areas = AreaManager.getAllAreas();
		Trainer trainer = new Trainer();
		trainer.setId(id);
		mv.addObject("trainer", trainer);
		mv.addObject("areas", areas);
		return mv;
	}
	
	@PostMapping("/addTrainer")
	public ModelAndView addNewTrainer(@ModelAttribute Trainer trainer, Model model) throws SQLException {
		DatabaseController.addTrainer(trainer);
		return viewTrainer(trainer.getId());
	}
	
	
	/**
	 * POKEMON
	 */
	@GetMapping("/addPokemon")
	public ModelAndView addPokemon(@RequestParam(value = "tId", required = true) int tId, Model model) throws SQLException {
		ModelAndView mv = new ModelAndView("addPokemon");
		int id = DatabaseController.getNextPokemonId();
		List<Species> species = DatabaseController.getAllSpecies();
		Pokemon pokemon = new Pokemon();
		pokemon.setId(id);
		pokemon.setTrainerId(tId);
		mv.addObject("pokemon", pokemon);
		mv.addObject("species", species);
		mv.addObject("trainerId", tId);
		return mv;
	}
	
	@PostMapping("/addPokemon")
	public ModelAndView addNewPokemon(@ModelAttribute Pokemon pokemon, Model model) throws SQLException {
		DatabaseController.addPokemon(pokemon);
		return viewTrainer(pokemon.getTrainerId());
	}
	
	@GetMapping("/editPokemon")
	public ModelAndView editPokemon(@RequestParam(value = "id", required = true) int id, Model model) throws SQLException {
		ModelAndView mv = new ModelAndView("editPokemon");
		Pokemon pokemon = DatabaseController.getPokemon(id);
		List<Species> species = DatabaseController.getAllSpecies();
		
		mv.addObject("pokemon", pokemon);
		mv.addObject("species", species);
		return mv;
	}
	
	@PostMapping("/editPokemon")
	public ModelAndView updatePokemon(@ModelAttribute Pokemon pokemon, Model model) throws SQLException {
		DatabaseController.updatePokemon(pokemon);
		return viewTrainer(pokemon.getTrainerId());
	}
	
	@GetMapping("/deletePokemon")
	public ModelAndView deletePokemon(@RequestParam(value = "pId", required = true) int pId, @RequestParam(value = "tId", required = true) int tId) throws SQLException {
		DatabaseController.deletePokemon(pId);
		return viewTrainer(tId);
	}
	
	
	/**
	 * MOVES
	 */
	@GetMapping("/addKnownMove")
	public ModelAndView addKnownMove(@RequestParam(value = "id", required = true) int id, Model model) throws SQLException {
		ModelAndView mv = new ModelAndView("addKnownMove");
		int tId = DatabaseController.getPokemon(id).getTrainerId();
		List<Move> moves = DatabaseController.getAllMoves();
		KnownMove km = new KnownMove();
		km.setPokemonId(id);
		mv.addObject("km", km);
		mv.addObject("moves", moves);
		mv.addObject("tId", tId);
		return mv;
	}
	
	@PostMapping("/addKnownMove")
	public ModelAndView addNewKnownMove(@ModelAttribute KnownMove km, Model model) throws SQLException {
		DatabaseController.addKnownMove(km);
		int tId = DatabaseController.getPokemon(km.getPokemonId()).getTrainerId();
		return viewTrainer(tId);
	}
	
	@GetMapping("/deleteKnownMove")
	public ModelAndView deleteKnownMove(@RequestParam(value = "moveId", required = true) int moveId, @RequestParam(value = "pokemonId", required = true) int pokemonId) throws SQLException {
		DatabaseController.deleteKnownMove(pokemonId, moveId);
		int tId = DatabaseController.getPokemon(pokemonId).getTrainerId();
		return viewTrainer(tId);
	}
	
	
	/**
	 * SPECIES
	 */
	@GetMapping("/species")
	public ModelAndView species(@RequestParam(value = "pokedexNum", required = true) int pokedexNum) throws SQLException {
		ModelAndView mv = new ModelAndView("species");
		Species result = DatabaseController.getSpecies(pokedexNum);
		List<Move> moves = DatabaseController.getLearnableMoves(pokedexNum);
		Species evolution;
		if (result.getEvolutionNum() != 0) {
			evolution = DatabaseController.getSpecies(result.getEvolutionNum());
		}
		else evolution = null;
		
		mv.addObject("species", result);
		mv.addObject("evolution", evolution);
		mv.addObject("moves", moves);
		return mv;
	}
	
	@GetMapping("/deleteSpecies")
	public String deleteSpecies(@RequestParam(value = "pokedexNum", required = true) int pokedexNum) throws SQLException {
		DatabaseController.deleteSpecies(pokedexNum);
		return "pokemon";
	}
	
	@GetMapping("/editSpecies")
	public ModelAndView editSpecies(@RequestParam(value = "pokedexNum", required = true) int pokedexNum) throws SQLException {
		ModelAndView mv = new ModelAndView("editSpecies");
		Species result = DatabaseController.getSpecies(pokedexNum);
		List<Species> allSpecies = DatabaseController.getAllSpecies();
		mv.addObject("species", result);
		mv.addObject("allSpecies", allSpecies);
		return mv;
	}
	
	@PostMapping("/editSpecies")
	public ModelAndView updateSpecies(@ModelAttribute Species species, Model model) throws SQLException {
		DatabaseController.updateSpecies(species);
		ModelAndView mv = new ModelAndView("species");
		Species result = DatabaseController.getSpecies(species.getPokedexNum());
		List<Move> moves = DatabaseController.getLearnableMoves(species.getPokedexNum());
		Species evolution;
		if (result.getEvolutionNum() != 0) {
			evolution = DatabaseController.getSpecies(result.getEvolutionNum());
		}
		else evolution = null;
		
		mv.addObject("species", result);
		mv.addObject("evolution", evolution);
		mv.addObject("moves", moves);
		return mv;
	}
	
	@GetMapping("/addSpecies")
	public ModelAndView addSpecies() throws SQLException {
		ModelAndView mv = new ModelAndView("addSpecies");
		int pokedexNum = DatabaseController.getNextPokedexNum();
		List<Species> allSpecies = DatabaseController.getAllSpecies();
		Species species = new Species();
		species.setPokedexNum(pokedexNum);
		mv.addObject("species", species);
		mv.addObject("allSpecies", allSpecies);
		return mv;
	}
	
	@PostMapping("/addSpecies")
	public ModelAndView submitAddSpecies(@ModelAttribute Species species, Model model) throws SQLException {
		DatabaseController.addSpecies(species);
		ModelAndView mv = new ModelAndView("species");
		List<Move> moves = DatabaseController.getLearnableMoves(species.getPokedexNum());
		Species evolution;
		if (species.getEvolutionNum() != 0) {
			evolution = DatabaseController.getSpecies(species.getEvolutionNum());
		}
		else evolution = null;
		
		mv.addObject("species", species);
		mv.addObject("evolution", evolution);
		mv.addObject("moves", moves);
		return mv;
	}
	
	@GetMapping("/moves")
	public ModelAndView moves() throws SQLException {
		ModelAndView mv = new ModelAndView("moves");
		List<Move> moves = DatabaseController.getAllMoves();
		
		mv.addObject("moves", moves);
		return mv;
	}
	
	@GetMapping("/viewMove")
	public ModelAndView viewMove(@RequestParam(value = "id", required = true) int id) throws SQLException {
		ModelAndView mv = new ModelAndView("viewMove");
		Move move = DatabaseController.getMove(id);
		List<Species> learnableSpecies = DatabaseController.getLearnableSpecies(id);
		
		mv.addObject("move", move);
		mv.addObject("learnableSpecies", learnableSpecies);
		return mv;
	}
	
	@GetMapping("/editMove")
	public ModelAndView editMove(@RequestParam(value = "id", required = true) int id) throws SQLException {
		ModelAndView mv = new ModelAndView("editMove");
		Move result = DatabaseController.getMove(id);
		List<Move> allMoves = DatabaseController.getAllMoves();
		mv.addObject("move", result);
		mv.addObject("allMoves", allMoves);
		return mv;
	}
	
	@PostMapping("/editMove")
	public ModelAndView updateMove(@ModelAttribute Move move, Model model) throws SQLException {
		DatabaseController.updateMove(move);
		return viewMove(move.getId());
	}
	
	@GetMapping("/deleteMove")
	public ModelAndView deleteMove(@RequestParam(value = "id", required = true) int id) throws SQLException {
		DatabaseController.deleteMove(id);
		return moves();
	}
	
	@GetMapping("/addMove")
	public ModelAndView addMove() throws SQLException {
		ModelAndView mv = new ModelAndView("addMove");
		int id = DatabaseController.getNextMoveId();
		List<Move> allMoves = DatabaseController.getAllMoves();
		Move move = new Move();
		move.setId(id);
		mv.addObject("move", move);
		mv.addObject("allMoves", allMoves);
		return mv;
	}
	
	@PostMapping("/addMove")
	public ModelAndView submitAddMoves(@ModelAttribute Move move, Model model) throws SQLException {
		DatabaseController.addMove(move);
		return viewMove(move.getId());
	}
}
