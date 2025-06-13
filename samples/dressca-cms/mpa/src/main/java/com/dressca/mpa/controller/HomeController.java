package com.dressca.mpa.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

  @GetMapping("/index")
	String home() {
		return "index";
	}

	@GetMapping("/accordion") 
	String accordion() {
		return "accordion";
	}

	@GetMapping("/basic")
	String basic() {
		return "basic";
	}
}
