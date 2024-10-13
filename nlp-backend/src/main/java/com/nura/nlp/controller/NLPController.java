package com.nura.nlp.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nura.nlp.service.NLPProcessService;
import com.nura.nlp.service.SynonymFinderService;

@RestController
@RequestMapping("/api/v1/nlp")
public class NLPController {

	private SynonymFinderService synonymFinder;
	private NLPProcessService nlpProcessService;

	public NLPController(SynonymFinderService synonymFinder, NLPProcessService nlpProcessService) {
		this.synonymFinder = synonymFinder;
		this.nlpProcessService = nlpProcessService;
	}

	@GetMapping("/{word}")
	public List<String> findSynonyms(@PathVariable String word) {
		return this.synonymFinder.findSynonym(word);
	}

	@GetMapping("/query/{queryString}")
	public void nlpProcess(@PathVariable String queryString) {
		this.nlpProcessService.processNLP(queryString);
	}

}
