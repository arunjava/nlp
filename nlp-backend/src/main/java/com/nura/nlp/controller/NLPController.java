package com.nura.nlp.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.nura.nlp.dto.FileRatingDTO;
import com.nura.nlp.service.NLPProcessService;
import com.nura.nlp.service.SynonymFinderService;

@RestController
@RequestMapping("/api/v1/nlp")
public class NLPController {

	@Value("${nlp.file.store.location}")
	private String nlpFileLocation;

	private SynonymFinderService synonymFinder;
	private NLPProcessService nlpProcessService;

	public NLPController(SynonymFinderService synonymFinder, NLPProcessService nlpProcessService) {
		this.synonymFinder = synonymFinder;
		this.nlpProcessService = nlpProcessService;
	}

	@GetMapping("/{word}")
	public List<String> findSynonyms(@PathVariable String word) {
		return this.synonymFinder.findSynonym(word);
	}

	@GetMapping("/query/{queryString}")
	public ResponseEntity<List<FileRatingDTO>> nlpProcess(@PathVariable String queryString) {
		List<FileRatingDTO> fileRatingList = this.nlpProcessService.processNLP(queryString);
		return ResponseEntity.status(HttpStatus.OK).body(fileRatingList);
	}

	@GetMapping("/list/files")
	public ResponseEntity<List<String>> getFileList() {
		List<String> fileNames = new ArrayList<>();
		File[] files = new File(nlpFileLocation).listFiles();
		for (File f : files) {
			fileNames.add(f.getName());
		}
		return ResponseEntity.status(HttpStatus.OK).body(fileNames);
	}

	@PostMapping("/upload")
	public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
		// Check if file is empty
		if (file.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please select a file to upload.");
		}

		if (nlpProcessService.uploadFile(file)) {
			return ResponseEntity.status(HttpStatus.OK).body("File Uploaded.");
		}

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File Upload Failed.");
	}
}
