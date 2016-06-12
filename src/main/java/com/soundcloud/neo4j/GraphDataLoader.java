package com.soundcloud.neo4j;

import static com.soundcloud.util.Constants.NAME;
import static com.soundcloud.util.Constants.PERSON;

import java.util.List;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

import com.soundcloud.neo4j.vo.Relation;
import com.soundcloud.util.Constants;

/**
 * Loads the data to graph db
 * 
 * @author Ashish
 *
 */
public class GraphDataLoader {

	private GraphDatabaseService graphDB;

	public GraphDataLoader(GraphDatabaseService graphDB) {
		this.graphDB = graphDB;
	}

	/**
	 * Create nodes and relationships in graph db for a given list of Relation
	 * VOs
	 * 
	 * @param relations
	 */
	public void loadData(List<Relation> relations) {
		try (Transaction tx = graphDB.beginTx()) {
			graphDB.schema().indexFor(PERSON).on(NAME).create();
			tx.success();
		}
		Transaction tx = graphDB.beginTx();

		for (int i = 0; i < relations.size(); i++) {
			createRelation(relations.get(i));
			
			//Logic for periodic commit every 1000 records...
			if (0 == (i+1) % 1000) {
				tx.success();
				tx.close();
				tx = graphDB.beginTx();
			}
		}
		tx.success();
		tx.close();
	}

	/**
	 * Establishes the relation between two nodes after creating/finding the
	 * nodes.
	 * 
	 * @param relation
	 */
	private void createRelation(Relation relation) {
		Node personA = createOrFindAndReturnNode(PERSON, relation.getPersonA());
		Node personB = createOrFindAndReturnNode(PERSON, relation.getPersonB());
		personA.createRelationshipTo(personB, relation.getRelationType());
	}

	/**
	 * Creates node if not present in graph and returns Finds and returns not if
	 * present in graph
	 * 
	 * @param person
	 * @param name
	 * @return
	 */
	private Node createOrFindAndReturnNode(Label person, String name) {
		Node n = graphDB.findNode(person, NAME, name);
		if (n == null) {
			n = graphDB.createNode(person);
			n.setProperty(NAME, name);
		}
		return n;
	}
}
