package com.nura.nlp.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.search.BooleanClause;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.nura.nlp.dto.FileRatingDTO;
import com.nura.nlp.lucene.LuceneFileIndexer;

@Service
public class NLPProcessService {

	@Value("${nlp.file.store.location}")
	private String nlpFileLocation;

	public List<FileRatingDTO> processNLP(String queryString) {
		List<FileRatingDTO> fileRatingList = new ArrayList<>();
		Map<BooleanClause.Occur, List<String>> queryParams = new HashMap<>();
		queryParams.put(BooleanClause.Occur.MUST, Arrays.asList(queryString.split(" ")));

		System.out.println(queryParams);

		try {
			Map<String, Float> fileRatings = LuceneFileIndexer.process(queryParams, nlpFileLocation);
			fileRatings.forEach((key, value) -> {
				FileRatingDTO fileRatingDTO = new FileRatingDTO();
				fileRatingDTO.setFileName(key.substring(nlpFileLocation.length(), key.length()));
				fileRatingDTO.setFileRatings(value);
				fileRatingList.add(fileRatingDTO);
			});
			System.out.println(fileRatings);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return fileRatingList;
	}

	public boolean uploadFile(MultipartFile file) {
		boolean isFileUploaded = false;
		try {
			// Ensure the upload directory exists
			Files.createDirectories(Paths.get(nlpFileLocation));

			// Get the file's original filename
			String originalFilename = file.getOriginalFilename();

			// Define the file path for saving
			Path filePath = Paths.get(nlpFileLocation + originalFilename);

			// Save the file on the server
			Files.write(filePath, file.getBytes());
			isFileUploaded = true;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return isFileUploaded;
	}
}
