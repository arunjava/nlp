package com.nura.nlp.filter;

import java.util.function.Predicate;

public class FileFilters {

	private FileFilters() {

	}

	public static final Predicate<String> textFileFilter = fileName -> fileName.endsWith(".txt");

}
