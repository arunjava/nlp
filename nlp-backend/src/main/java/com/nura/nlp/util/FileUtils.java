package com.nura.nlp.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * File related utilities
 */
public class FileUtils {

	private FileUtils() {

	}

	public static String readFileContent(String filePath) {
		try {
			return new String(Files.readAllBytes(Paths.get(filePath)));
		} catch (IOException e) {
			return "";
		}
	}

}
