package com.nura.nlp.stanford;

import java.util.Properties;

import com.nura.nlp.util.FileUtils;
import com.nura.nlp.util.SentimentResult;

import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SentimentAnalysis {

	private SentimentAnalysis() {
	}

	public static SentimentResult analyzeSentiment(String filePath) {
		log.info("Sentiment analysis for file : {}", filePath);
		// Set up properties for the pipeline
		Properties props = new Properties();
		props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner,parse,sentiment");

		// Create a CoreNLP pipeline with the specified properties
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

		// Input text
		String text = FileUtils.readFileContent(filePath);

		// Create a document object
		CoreDocument document = new CoreDocument(text);

		// Annotate the document
		pipeline.annotate(document);

		// Sentiment analysis for each sentence
		for (CoreSentence sentence : document.sentences()) {
			String sentiment = sentence.sentiment();
			log.info("Sentiment: {}", sentiment);
			if (sentiment.equalsIgnoreCase(SentimentResult.NEGATIVE.toString())) {
				return SentimentResult.NEGATIVE;
			}
		}

		return SentimentResult.POSITIVE;
	}

}
