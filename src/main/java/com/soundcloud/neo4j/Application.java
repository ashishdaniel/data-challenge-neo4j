package com.soundcloud.neo4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.soundcloud.neo4j.vo.Relation;
import com.soundcloud.util.Constants;
import com.soundcloud.util.FileUtil;

/**
 * Entry point for the application
 * 
 * @author Ashish
 *
 */
public class Application {

	private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) throws IOException {

		String inputFile = System.getProperty("input.file", "data/input_data");
		int degree = Integer.parseInt(System.getProperty("degree", "2"));
		String outputFile = System.getProperty("output.file", "output_data_neo4j");
		
		if(null == inputFile || !new File(inputFile).exists()) {
			LOGGER.error("{} invalid input file...", inputFile);
			System.exit(1);
		}

		LOGGER.info("input.file={}, output.file={}, degree={}", inputFile, outputFile, degree);

		List<Relation> relations = Files.lines(Paths.get(inputFile)).map(line -> line.split(Constants.TAB_CHAR))
				.map(persons -> new Relation(persons[0], persons[1], Constants.FRIEND_OF)).collect(Collectors.toList());

		LOGGER.info("File Read Done...");
		
		File f = Files.createTempDirectory("tmp-graph").toFile();
		GraphDatabaseService graphDB = new GraphDatabaseFactory().newEmbeddedDatabaseBuilder(f).newGraphDatabase();

		GraphDataLoader loader = new GraphDataLoader(graphDB);
		loader.loadData(relations);

		LOGGER.info("Graph Data Load Done...");

		GraphDataExtractor extractor = new GraphDataExtractor(graphDB);
		Stream<List<String>> resultStream = extractor.extractResultStream(degree);
		FileUtil.writeToFileFromStream(resultStream, outputFile);
		LOGGER.info("Written result to {} ", outputFile);

		// clean up
		graphDB.shutdown();
	}

}