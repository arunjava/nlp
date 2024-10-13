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

	public static String readFileContent(String filePath) throws IOException {
		return new String(Files.readAllBytes(Paths.get(filePath)));
	}

	public static void main(String[] args) throws IOException {
		String filecontent = readFileContent("C:\\Temp\\Data\\Job Openings\\java opening.txt");
		System.out.println(filecontent);
	}
}
