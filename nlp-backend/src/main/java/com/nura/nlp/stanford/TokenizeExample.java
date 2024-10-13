package com.nura.nlp.stanford;

import java.util.Properties;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

public class TokenizeExample {

	public static void main(String[] args) {
		// Set up properties for the pipeline
		Properties props = new Properties();
		props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner,parse,sentiment");

		// Create a CoreNLP pipeline with the specified properties
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

		// Input text
		String text = "Anna University is located in Chennai. Job openings filled in the university. Opening in hospital";

		// Create a document object
		CoreDocument document = new CoreDocument(text);

		// Annotate the document
		pipeline.annotate(document);

		// Display tokens, named entities, and parse tree
		for (CoreLabel token : document.tokens()) {
			System.out.println(token.word() + " (" + token.ner() + ")");
		}

		// Sentiment analysis for each sentence
		for (CoreSentence sentence : document.sentences()) {
			String sentiment = sentence.sentiment();
			System.out.println("Sentiment: " + sentiment);
		}
	}
}
