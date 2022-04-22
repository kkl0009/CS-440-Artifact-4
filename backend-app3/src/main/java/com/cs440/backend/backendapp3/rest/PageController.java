package com.cs440.backend.backendapp3.rest;

import java.sql.SQLException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
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
		Species evolution = DatabaseController.getSpecies(result.getEvolutionNum());
		mv.addObject("species", result);
		mv.addObject("evolution", evolution);
		return mv;
	}
	
}
