package com.nura.nlp.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.nura.nlp.dto.SynonymDTO;

@Service
public class SynonymFinderService {

	private RestTemplate restTemplate;

	public SynonymFinderService(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public List<String> findSynonym(String word) {
		SynonymDTO[] synonymDTOs = this.restTemplate.getForObject("https://api.datamuse.com/words?ml=" + word,
				SynonymDTO[].class);
		List<String> synonyms = new ArrayList<>();
		for (SynonymDTO synonym : synonymDTOs) {
			synonyms.add(synonym.getWord());
		}
		return synonyms.subList(0, 5);
	}

}
