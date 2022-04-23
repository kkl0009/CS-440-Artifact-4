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

import com.cs440.backend.backendapp3.objects.Area;
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
		List<Area> areas = DatabaseController.getAllAreas();
		
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
	
	@GetMapping("/viewArea")
	public ModelAndView viewArea(@RequestParam(value = "id", required = true) int id) throws SQLException {
		ModelAndView mv = new ModelAndView("viewArea");
		Area area = DatabaseController.getArea(id);
		List<Area> adjacentAreas = DatabaseController.getAdjacentAreas(id);
		List<String> landmarks = DatabaseController.getLandmarks(id);
		List<SpeciesAndSpawnRate> spawns = DatabaseController.getSpawns(id);
		List<Trainer> trainers = DatabaseController.getTrainersInArea(id);
		
		mv.addObject("area", area);
		mv.addObject("adjacentAreas", adjacentAreas);
		mv.addObject("landmarks", landmarks);
		mv.addObject("spawns", spawns);
		mv.addObject("trainers", trainers);
		return mv;
	}
	
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
	
	@GetMapping("/pokemon")
	public String pokemon() {
		return "pokemon";
	}
	
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
	
}
