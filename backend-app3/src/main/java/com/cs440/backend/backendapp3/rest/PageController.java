package com.cs440.backend.backendapp3.rest;

import java.sql.SQLException;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.cs440.backend.backendapp3.objects.Species;

@Controller
public class PageController {

	@GetMapping("/areas")
	public String areas() {
		return "areas";
	}
	
	@GetMapping("/pokemon")
	public String pokemon() {
		return "pokemon";
	}
	
	@GetMapping("/species")
	public ModelAndView species(@RequestParam(value = "pokedexNum", required = true) int pokedexNum) throws SQLException {
		ModelAndView mv = new ModelAndView("species");
		Species result = DatabaseController.getSpecies(pokedexNum);
		Species evolution;
		if (result.getEvolutionNum() != 0) {
			evolution = DatabaseController.getSpecies(result.getEvolutionNum());
		}
		else evolution = null;
		
		mv.addObject("species", result);
		mv.addObject("evolution", evolution);
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
		Species evolution;
		if (result.getEvolutionNum() != 0) {
			evolution = DatabaseController.getSpecies(result.getEvolutionNum());
		}
		else evolution = null;
		
		mv.addObject("species", result);
		mv.addObject("evolution", evolution);
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
		Species evolution;
		if (species.getEvolutionNum() != 0) {
			evolution = DatabaseController.getSpecies(species.getEvolutionNum());
		}
		else evolution = null;
		
		mv.addObject("species", species);
		mv.addObject("evolution", evolution);
		return mv;
	}
	
}
