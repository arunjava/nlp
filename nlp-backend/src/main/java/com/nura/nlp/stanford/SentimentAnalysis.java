package com.nura.nlp.stanford;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SentimentAnalysis {

	private SentimentAnalysis() {
	}

	public static Map<String, Integer> analyzeSentiment(String filePath) {
		log.info("Sentiment analysis for file : {}", filePath);
		// Set up properties for the pipeline
		Properties props = new Properties();
		props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner,parse,sentiment");

		// Create a CoreNLP pipeline with the specified properties
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

//		// Input text
//		String text = FileUtils.readFileContent(filePath);
//
//		// Create a document object
//		CoreDocument document = new CoreDocument(text);
//
//		// Annotate the document
//		pipeline.annotate(document);
//
//		// Sentiment analysis for each sentence
//		for (CoreSentence sentence : document.sentences()) {
//			String sentiment = sentence.sentiment();
//			if (sentiment.equalsIgnoreCase(SentimentResult.NEGATIVE.toString())) {
//				log.info("Sentiment line : {}", sentence);
//				return SentimentResult.NEGATIVE;
//			}
//		}
//
//		return SentimentResult.POSITIVE;
		// Map to store keywords and their sentiment scores
		Map<String, Integer> keywordSentiments = new HashMap<>();

		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			String line;
			long lineNumer = 0;
			while ((line = reader.readLine()) != null) {
				log.info("Analyzing line content : {}", line);
				CoreDocument document = new CoreDocument(line);

				// Annotate the document
				pipeline.annotate(document);

				// Sentiment analysis for each sentence
				for (CoreSentence sentence : document.sentences()) {
					String sentiment = sentence.sentiment();
					int sentimentScore = getSentimentScore(sentiment); // Convert sentiment to numeric score

					for (CoreLabel token : sentence.tokens()) {
						String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
						if (pos.startsWith("NN") || pos.startsWith("JJ")) { // Nouns and adjectives as keywords
							keywordSentiments.put(token.word(),
									keywordSentiments.getOrDefault(token.word(), 0) + sentimentScore);
						}
					}

//					if (sentiment.equalsIgnoreCase(SentimentResult.NEGATIVE.toString())) {
//						log.info("Sentiment line : {}", sentence);
//						return SentimentResult.NEGATIVE;
//					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		log.info("Sentiment result -> {}" + keywordSentiments);
		
//		return SentimentResult.POSITIVE;
		return keywordSentiments;
	}

	// Helper method to convert sentiment label to a numeric score
	public static int getSentimentScore(String sentiment) {
		switch (sentiment) {
		case "Very Positive":
			return 2;
		case "Positive":
			return 1;
		case "Neutral":
			return 0;
		case "Negative":
			return -1;
		case "Very Negative":
			return -2;
		default:
			return 0;
		}
	}
}
