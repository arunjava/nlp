package com.nura.nlp.stanford;

import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.util.PropertiesUtils;

import java.util.*;

public class NLPMatchingExample {
	public static void main(String[] args) {
		// Step 1: Set up the Stanford CoreNLP pipeline
		Properties props = new Properties();
		props.setProperty("annotators", "tokenize,ssplit,pos,lemma");
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

		// Step 2: Define the input text and keywords for matching
		String text = "Stanford University is a great place to acquire knowledge NLP and artificial intelligence.";
		List<String> keywords = Arrays.asList("Stanford", "knowledge", "intelligent");

		// Step 3: Annotate the text using the CoreNLP pipeline
		CoreDocument document = new CoreDocument(text);
		pipeline.annotate(document);

		// Step 4: Find matching words based on their lemmas
		List<String> matches = new ArrayList<>();
		for (CoreLabel token : document.tokens()) {
			String word = token.word();
			String lemma = token.lemma(); // Lemma (root form of the word)

			// Check if the lemma matches any of the keywords
			if (keywords.contains(lemma)) {
				matches.add(word); // Add original word if match is found
			}
		}

		// Step 5: Output the matches
		System.out.println("Matching words: " + matches);
	}
}
