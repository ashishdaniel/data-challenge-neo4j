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
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import com.soundcloud.neo4j.GraphDataLoader;
import com.soundcloud.neo4j.vo.Relation;
import com.soundcloud.util.Constants;

/**
 * Test class for GraphDataLoad
 * 
 * @author Ashish
 *
 */
public class GraphDataLoadTest {

	private static GraphDatabaseService graphDB;

	@BeforeClass
	public static void init() throws IOException {
		graphDB = new GraphDatabaseFactory()
				.newEmbeddedDatabaseBuilder(Files.createTempDirectory("tmp-graph-load").toFile()).newGraphDatabase();

	}

	/**
	 * For testing graph data is loaded.
	 */
	@Test
	public void testGraphDataLoad() {
		List<Relation> relations = Arrays.asList(new Relation("davidbowie", "omid", FRIEND_OF));
		new GraphDataLoader(graphDB).loadData(relations);
		try (Transaction tx = graphDB.beginTx()) {
			Node n = graphDB.findNode(Constants.PERSON, "name", "davidbowie");
			assertTrue("davidbowie".equals(n.getProperty("name")));
		}
	}

	@AfterClass
	public static void destroy() {
		graphDB.shutdown();
	}
}
