package com.nura.nlp.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.search.BooleanClause;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.nura.nlp.lucene.LuceneFileIndexer;

@Service
public class NLPProcessService {

	@Value("${nlp.file.store.location}")
	private String nlpFileLocation;

	public void processNLP(String queryString) {
		Map<BooleanClause.Occur, List<String>> queryParams = new HashMap<>();
		queryParams.put(BooleanClause.Occur.MUST, Arrays.asList(queryString.split(" ")));

		System.out.println(queryParams);

		try {
			Map<String, Float> fileRatings = LuceneFileIndexer.process(queryParams, nlpFileLocation);

			System.out.println(fileRatings);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
