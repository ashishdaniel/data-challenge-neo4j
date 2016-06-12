package com.soundcloud.test;

import static com.soundcloud.util.Constants.FRIEND_OF;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import com.soundcloud.neo4j.GraphDataExtractor;
import com.soundcloud.neo4j.GraphDataLoader;
import com.soundcloud.neo4j.vo.Relation;

/**
 * Test class for GraphDataExtractor
 * 
 * @author Ashish
 *
 */
public class GraphDataExtractorTest {

	private static GraphDatabaseService graphDB;

	@BeforeClass
	public static void init() throws IOException {
		graphDB = new GraphDatabaseFactory()
				.newEmbeddedDatabaseBuilder(Files.createTempDirectory("tmp-graph-extract").toFile()).newGraphDatabase();
		List<Relation> relations = Arrays.asList(new Relation("davidbowie", "omid", FRIEND_OF),
				new Relation("omid", "kim", FRIEND_OF));
		new GraphDataLoader(graphDB).loadData(relations);
	}

	/**
	 * For testing negative degree results.
	 * Expects a IllegalArgumentException
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testGraphForInvalidDegree() {
		new GraphDataExtractor(graphDB).extractResult(-1);
	}

	/**
	 * For testing first degree results.
	 */
	@Test
	public void testGraphForFirstDegree() {
		List<List<String>> result = new GraphDataExtractor(graphDB).extractResult(1);
		List<String> expectedResult = Arrays.asList("davidbowie", "omid");
		List<String> firstResult = result.get(0);
		assertTrue(firstResult.size() == expectedResult.size());
		firstResult.removeAll(expectedResult);
		assertTrue(firstResult.size() == 0);
	}

	/**
	 * For testing second degree results.
	 */
	@Test
	public void testGraphForSecondDegree() {
		List<List<String>> result = new GraphDataExtractor(graphDB).extractResult(2);
		List<String> expectedResult = Arrays.asList("davidbowie", "omid", "kim");
		List<String> firstResult = result.get(0);
		assertTrue(firstResult.size() == expectedResult.size());
		firstResult.removeAll(expectedResult);
		assertTrue(firstResult.size() == 0);
	}

	@AfterClass
	public static void destroy() {
		graphDB.shutdown();
	}
}
