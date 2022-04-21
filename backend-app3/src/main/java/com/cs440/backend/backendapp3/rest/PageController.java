package com.cs440.backend.backendapp3.rest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

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
	
}
