package com.cs440.backend.backendapp3.rest;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cs440.backend.backendapp3.objects.ExampleObject;

@RestController
public class ExampleController {

	private static final String template = "Hello, %s!";
	private final AtomicLong counter = new AtomicLong();

	@GetMapping("/greeting")
	public ExampleObject greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
		return new ExampleObject(counter.incrementAndGet(), String.format(template, name));
	}
}