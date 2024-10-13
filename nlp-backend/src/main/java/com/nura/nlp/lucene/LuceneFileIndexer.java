package com.nura.nlp.lucene;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import com.nura.nlp.util.LuceneConstants;

public class LuceneFileIndexer {

	private static final String INDEX_DIR = "indexDir";

	// Indexing the files
	public static Map<String, Float> process(Map<BooleanClause.Occur, List<String>> queryParams, String filePath)
			throws Exception {
		Directory dir = FSDirectory.open(Paths.get(INDEX_DIR));
		StandardAnalyzer analyzer = new StandardAnalyzer();
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		config.setOpenMode(IndexWriterConfig.OpenMode.CREATE); // Overwrite the existing index
		IndexWriter writer = new IndexWriter(dir, config);

		File[] files = new File(filePath).listFiles();
		if (files != null) {
			for (File file : files) {
				indexFile(writer, file);
			}
		}
		writer.close();

		// Now, you can search indexed files
		Map<String, Float> fileRatings = searchIndex(queryParams);

		return fileRatings;
	}

	// Method to index a single file
	private static void indexFile(IndexWriter writer, File file) throws IOException {
		Document doc = new Document();

		// File content to be indexed
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			StringBuilder contentBuilder = new StringBuilder();
			String line;
			while ((line = br.readLine()) != null) {
				contentBuilder.append(line).append("\n");
			}
			doc.add(new TextField("content", contentBuilder.toString(), Field.Store.YES));
		}

		// File path
		doc.add(new StringField("path", file.getAbsolutePath(), Field.Store.YES));
		writer.addDocument(doc);
	}

	// Method to search the indexed files
	private static Map<String, Float> searchIndex(Map<BooleanClause.Occur, List<String>> queryParams) throws Exception {
		Map<String, Float> fileRatings = new LinkedHashMap<>();
		Directory dir = FSDirectory.open(Paths.get(INDEX_DIR));
		DirectoryReader reader = DirectoryReader.open(dir);
		IndexSearcher searcher = new IndexSearcher(reader);
		StandardAnalyzer analyzer = new StandardAnalyzer();

		BooleanQuery.Builder booleanQuery = new BooleanQuery.Builder();
		queryParams.forEach((key, value) -> {
			if (key == BooleanClause.Occur.MUST) {
				value.forEach(queryString -> {
					try {
						booleanQuery.add(new QueryParser(LuceneConstants.CONTENT, analyzer).parse(queryString),
								BooleanClause.Occur.MUST);
					} catch (ParseException e) {
						e.printStackTrace();
					}
				});
			}
		});

		System.out.println("Boolean query : " + booleanQuery.build().toString());

		TopDocs results = searcher.search(booleanQuery.build(), 10);

		for (ScoreDoc hit : results.scoreDocs) {
			Document doc = searcher.doc(hit.doc);
			fileRatings.put(doc.get("path"), hit.score);
		}

		reader.close();

		return fileRatings;
	}

	public static void main(String[] args) throws Exception {
		Map<BooleanClause.Occur, List<String>> queryParams = new HashMap<>();
		queryParams.put(BooleanClause.Occur.MUST, Arrays.asList("job"));
//		queryParams.put(BooleanClause.Occur.SHOULD, Arrays.asList("java"));
		process(queryParams, "C:\\Temp\\Data\\Job Openings");
	}
}
