package com.soundcloud.neo4j;

import static com.soundcloud.util.Constants.CONNECTIONS;
import static com.soundcloud.util.Constants.NAME;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Result;

/**
 * Extracts the result from graph
 * 
 * @author Ashish
 *
 */
public class GraphDataExtractor {

	private GraphDatabaseService graphDB;

	/**
	 * Query for getting nth degree of friends
	 */
	private String query = "MATCH (p1:Person)-[:FRIEND_OF*..%d]-(p2:Person) "
			+ "RETURN p1.name as name,collect(distinct(p2.name)) as connections " + "ORDER BY p1.name";

	public GraphDataExtractor(GraphDatabaseService graphDB) {
		this.graphDB = graphDB;
	}

	/**
	 * The function for merging name and connections from query result
	 */
	@SuppressWarnings("unchecked")
	private Function<Map<String, Object>, List<String>> extractResultFn = (resultMap) -> {
		List<String> list = new LinkedList<>((List<String>) resultMap.get(CONNECTIONS));
		Collections.sort(list);
		list.add(0, (String) resultMap.get(NAME));
		return list;
	};

	/**
	 * Extracts the result for nth degree of friendship
	 * 
	 * @param degree
	 * @return
	 */
	public List<List<String>> extractResult(int degree) {

		return extractResultStream(degree).collect(Collectors.toList());
	}
	
	/**
	 * Extracts the result stream for nth degree of friendship
	 * 
	 * @param degree
	 * @return
	 */
	public Stream<List<String>> extractResultStream(int degree) {
		if (degree < 0) {
			throw new IllegalArgumentException("Please provided a positive integer as degree");
		}
		
		Result result = graphDB.execute(String.format(query, degree));
		return result.stream().map(extractResultFn);
	}

}
