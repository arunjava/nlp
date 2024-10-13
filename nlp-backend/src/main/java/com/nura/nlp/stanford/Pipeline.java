package com.nura.nlp.stanford;

import java.util.Properties;

import edu.stanford.nlp.pipeline.StanfordCoreNLP;

public class Pipeline {

	private static Properties properties;
	private static String propertieName = "";
	private static StanfordCoreNLP stanfordCoreNLP;

	private Pipeline() {

	}

	static {
		properties = new Properties();
		properties.setProperty("annotators", propertieName);
	}
	
	public static StanfordCoreNLP getPipeline() {
		if (stanfordCoreNLP == null) {
			stanfordCoreNLP = new StanfordCoreNLP(properties);
		}

		return stanfordCoreNLP;
	}

}
