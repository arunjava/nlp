package com.nura.nlp.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.nura.nlp.dto.SynonymDTO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SynonymFinderService {

	private RestTemplate restTemplate;

	public SynonymFinderService(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public List<String> findSynonym(String word) {
//		https://api.datamuse.com/words?rel_syn=job
//		https://api.datamuse.com/words?ml=
		log.info("Finding synoym for word : {}", word);
		SynonymDTO[] synonymDTOs = this.restTemplate.getForObject("https://api.datamuse.com/words?rel_syn=" + word,
				SynonymDTO[].class);
		List<String> synonyms = new ArrayList<>();
		for (SynonymDTO synonym : synonymDTOs) {
			synonyms.add(synonym.getWord());
		}
		log.info("Synonym result : {}", synonyms);
		return synonyms.subList(0, synonyms.size() > 5 ? 5 : synonyms.size());
	}
}
