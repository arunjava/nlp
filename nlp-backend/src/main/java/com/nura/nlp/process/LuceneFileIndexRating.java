package com.nura.nlp.process;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.store.ByteBuffersDirectory;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.BytesRef;

import com.nura.nlp.util.FileUtils;

public class LuceneFileIndexRating {

	private Directory indexDirectory;
	private IndexWriter indexWriter;

	public LuceneFileIndexRating() throws IOException {
		// Use ByteBuffersDirectory instead of RAMDirectory for in-memory indexing
		indexDirectory = new ByteBuffersDirectory();
		StandardAnalyzer analyzer = new StandardAnalyzer();
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		indexWriter = new IndexWriter(indexDirectory, config);
	}

	public void addDocument(String content) throws IOException {
		Document doc = new Document();
		doc.add(new TextField("content", content, Field.Store.YES));
		indexWriter.addDocument(doc);
	}

	public void close() throws IOException {
		indexWriter.close();
	}

	public Directory getIndexDirectory() {
		return indexDirectory;
	}

	public float searchSimilarWordsWithScores(String word, int maxEdits) throws IOException {
		float score = 0.0f;
		IndexReader reader = DirectoryReader.open(indexDirectory);
		IndexSearcher searcher = new IndexSearcher(reader);
		searcher.setSimilarity(new ClassicSimilarity());

		// FuzzyQuery to find words similar to the input term
		FuzzyQuery fuzzyQuery = new FuzzyQuery(new Term("content", word), maxEdits);

		// Perform search
		TopDocs results = searcher.search(fuzzyQuery, 10);
		System.out.println("Found " + results.totalHits + " similar terms for: " + word);

		for (ScoreDoc scoreDoc : results.scoreDocs) {
			Document doc = searcher.doc(scoreDoc.doc);
//			System.out.println("Document: " + doc.get("content") + ", Score: " + scoreDoc.score);
//			System.out.println("Score: " + scoreDoc.score);
			score = scoreDoc.score;
			// Get Term Frequency and calculate TF-IDF for the matching terms
			Terms termVector = reader.getTermVector(scoreDoc.doc, "content");
			if (termVector != null) {
				TermsEnum termsEnum = termVector.iterator();
				BytesRef term = null;
				while ((term = termsEnum.next()) != null) {
					String termText = term.utf8ToString();
					long termFreq = termsEnum.totalTermFreq();
					long docFreq = reader.docFreq(new Term("content", termText));

					ClassicSimilarity similarity = new ClassicSimilarity();
					float tf = similarity.tf(termFreq);
					float idf = similarity.idf(docFreq, reader.numDocs());
					float tfIdf = tf * idf;

					System.out.println("   Term: " + termText + ", TF-IDF: " + tfIdf);
				}
			}
		}

		reader.close();

		return score;
	}

	public static void main(String[] args) throws IOException {
		LuceneFileIndexRating example = new LuceneFileIndexRating();
//		example.addDocument(
//				"We need new employee for hospital. Job Openings for hospital. This opening is for new roles");
//		example.addDocument("Employee are not good");
//		example.addDocument("Lucene is great for searching text. No job openings");
//		example.addDocument("NLP helps machines understand language and hospital.");

		File[] files = new File("C:\\Temp\\Data\\Job Openings").listFiles();
		for (File f : files) {
			example.addDocument(FileUtils.readFileContent(f.getAbsolutePath()));
		}

		example.close();

		example.searchSimilarWordsWithScores("Job", 2); // Searching for words similar to 'processing'
	}

}
