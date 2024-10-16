package com.nura.nlp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * PageController handles HTTP requests for the application's web pages. It
 * serves as a central point for routing to the appropriate views, particularly
 * for Single Page Applications (SPAs).
 *
 * This controller defines the following endpoints: The controller ensures that
 * all relevant paths are routed correctly to the front-end application,
 * maintaining the SPA functionality.
 */
@Controller
@RequestMapping(value = "/")
public class PageController {
	/**
	 * Handles requests to the home page.
	 * 
	 * @return A string that forwards the request to index.html.
	 */
	@GetMapping(value = "/")
	public String index() {
		return "forward:/index.html";
	}

	/**
	 * Handles all other requests that do not contain a file extension.
	 * 
	 * @return A string that forwards the request to index.html, allowing the SPA to
	 *         handle routing.
	 */
//	@GetMapping(value = "/**/{path:[^.]*}")
//	public String other() {
//		return "forward:/index.html";
//	}
}
